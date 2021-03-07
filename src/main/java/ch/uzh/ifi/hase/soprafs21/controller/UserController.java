package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.AuthenticationRequest;
import ch.uzh.ifi.hase.soprafs21.entity.AuthenticationResponse;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.util.JwtUtil;
import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.mapstruct.BeanMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.naming.AuthenticationException;

/**
 * User Controller This class is responsible for handling all REST request that
 * are related to the user. The controller will receive the request and delegate
 * the execution to the UserService and finally return the result.
 */
@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/auth/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authReq)
            throws AuthenticationException {
        try {
            String username = authReq.getUsername();
            String password = authReq.getPassword();
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Incorrect username or password");
        }
        final UserDetails userDetails = userService.loadUserByUsername(authReq.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }

    @GetMapping("/auth/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @PostMapping("/auth/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@NonNull @RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation

        List<User> users = userService.getUsers();
        for (User user : users) {
            if (userPostDTO.getUsername().equals(user.getUsername())) {
                logger.info("THROWING ERROR");
                throw new IllegalArgumentException("Username already taken!");
            }
        }

        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @GetMapping("/auth/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserById(@PathVariable String id) {
        User user;
        long idAsNumber = Long.parseLong(id);
        try {
            user = userService.getUserById(idAsNumber);
            return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
        } catch (Exception e) {
            throw new UserNotFoundException(idAsNumber);
        }
        // return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    // @PutMapping("/users/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void putUser(long id, long token) {
    // // TODO
    // }

    // @PostMapping("/login")
    // @ResponseStatus(HttpStatus.OK)
    // public void login(@RequestBody loginData){
    // try{
    // User user = DTOMapper.INSTANCE.convertEntityToUserGetDTO(loginData);
    // String token = UserService.login(user);
    // }

    // }

}
