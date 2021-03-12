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
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("api/v1/")
public class UserController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    private static JwtUtil jwtTokenUtil = new JwtUtil();

    Logger logger = LoggerFactory.getLogger(UserController.class);

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/authenticate")
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

        final User user = userService.loadUserByUsername(authReq.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt, user));

    }

    @GetMapping("/users")
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

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@NonNull @RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User createdUser = userService.createUser(userInput);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserById(@PathVariable String id) {
        User user;

        try {
            user = userService.getUserById(id);
            return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
        } catch (Exception e) {
            throw new UserNotFoundException(id);
        }
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserGetDTO updateUser(@PathVariable String id, @RequestBody UserPostDTO userPostDTO,
            @RequestHeader("Authorization") String auth) {
        // try {
        User newUser = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check if user is trying to change another users username
        if (!userService.idAndTokenMatch(id, auth.substring(7))) {
            throw new IllegalArgumentException("Token and user do not match!");
        }

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userService.updateUser(id, newUser));
        // } catch (Exception e) {
        // throw new UserNotFoundException(id);

        // }
    }
}
