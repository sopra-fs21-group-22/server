package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserAuthGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserAuthPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Autowired
    private JwtUtil jwtTokenUtil;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public UserAuthGetDTO createAuthToken(@RequestBody UserAuthPostDTO userAuthPostDTO) throws AuthenticationException {

        String username = userAuthPostDTO.getUsername();
        String password = userAuthPostDTO.getPassword();
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        final UserDetails userDetails = userService.loadUserByUsername(userAuthPostDTO.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        User user = userService.loadUserByUsername(userAuthPostDTO.getUsername());
        user.setJwt(jwt);
        userService.updateUserStatus(user.getId(), UserStatus.ONLINE);

        return DTOMapper.INSTANCE.convertEntityToUserAuthGetDTO(user);
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
    public UserGetDTO getUserById(@PathVariable Long id) {
        User user;
        user = userService.getUserById(id);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserGetDTO updateUser(@PathVariable Long id, @RequestBody UserPutDTO userPutDTO,
            @RequestHeader("Authorization") String auth) {
        // check if user is trying to change another users username
        if (!userService.idAndTokenMatch(id, auth.substring(7))) {
            throw new IllegalArgumentException("Token and user do not match!");
        }

        User newUser = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userService.updateUser(id, newUser));
    }
}
