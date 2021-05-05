package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.HandRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.repository.CharacterCardPileRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserPutDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameRoleService;
import ch.uzh.ifi.hase.soprafs21.service.OnFieldCardsService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.util.JwtUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest This is a WebMvcTest which allows to test the
 * UserController i.e. GET/POST request without actually sending them over the
 * network. This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
@ComponentScan("ch.uzh.ifi.hase.soprafs21")
public class UserControllerTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    // most of these @MockBean entries are only here to prevent an annoying bug when
    // running tests

    @MockBean
    private UserService userService;

    @MockBean
    private PlayerTableRepository playerTableRepository;

    @MockBean
    private PlayerRepository PlayerRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    DeckRepository deckRepository;

    @MockBean
    HandRepository handRepository;

    @MockBean
    OnFieldCardsService onFieldCardsService;

    @MockBean
    CharacterCardPileRepository characterCardPileRepository;

    private User user;

    private String baseUrl;

    @BeforeEach
    public void beforeEach() {
        baseUrl = "/api/v1";
        user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setId(1L);
        user.setStatus(UserStatus.OFFLINE);
    }

    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);
        given(userService.loadUserByUsername(Mockito.any())).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get(String.format("%s/users", baseUrl))
                .contentType(MediaType.APPLICATION_JSON).header("Authorization", getHeader(user));

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
    }

    @Test
    public void createUser_validInput_userCreated() throws Exception {
        // given

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post(String.format("%s/users", baseUrl))
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }

    @Test
    public void createUser_invalidInput_userCreated() throws Exception {
        // given

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("");
        userPostDTO.setPassword("");

        given(userService.createUser(Mockito.any())).willThrow(IllegalArgumentException.class);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post(String.format("%s/users", baseUrl))
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest).andExpect(status().isConflict());
    }

    @Test
    public void getUserById_successfully() throws Exception {

        given(userService.loadUserByUsername(Mockito.any())).willReturn(user);
        given(userService.getUserById(user.getId())).willReturn(user);

        Date creationDate = new Date(System.currentTimeMillis());
        user.setCreationDate(creationDate);

        MockHttpServletRequestBuilder getRequest = get(String.format("%s/users/%s", baseUrl, user.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON).header("Authorization", getHeader(user));

        mockMvc.perform(getRequest).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.creationDate", is(user.getCreationDate().toString())));
    }

    // @Test
    // public void getUserById_unsuccessfully_userNotExistent() throws Exception {

    // given(userRepository.findById(Mockito.any())).willThrow(UserNotFoundException.class);
    // given(userService.loadUserByUsername(Mockito.any())).willReturn(user);
    // //
    // given(userService.getUserById(user.getId())).willThrow(UserNotFoundException.class);

    // Date creationDate = new Date(System.currentTimeMillis());
    // user.setCreationDate(creationDate);

    // MockHttpServletRequestBuilder getRequest = get(String.format("%s/users/222",
    // baseUrl))
    // .contentType(MediaType.APPLICATION_JSON).header("Authorization",
    // getHeader(user));

    // mockMvc.perform(getRequest).andExpect(status().isNotFound());
    // }

    // @Test
    // public void getUserById_unauthorized_isForbidden() throws Exception {
    // MockHttpServletRequestBuilder getRequest = get(String.format("%s/users/1",
    // baseUrl))
    // .contentType(MediaType.APPLICATION_JSON);

    // mockMvc.perform(getRequest).andExpect(status().isForbidden());
    // }

    @Test
    public void updateUser_successfully() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setStatus(UserStatus.OFFLINE);

        given(userService.loadUserByUsername(Mockito.any())).willReturn(user);
        given(userService.getUserById(Mockito.any())).willReturn(user);
        given(userService.idAndTokenMatch(Mockito.any(), Mockito.any())).willReturn(true);
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user));

        MockHttpServletRequestBuilder putRequest = put(String.format("%s/users/%s", baseUrl, user.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userPutDTO))
                .header("Authorization", getHeader(user));

        mockMvc.perform(putRequest).andExpect(status().isNoContent());

    }

    @Test
    public void updateUser_otherUserThanSelf_throws() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setStatus(UserStatus.OFFLINE);

        given(userService.loadUserByUsername(Mockito.any())).willReturn(user);
        given(userService.getUserById(Mockito.any())).willReturn(user);
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user));

        MockHttpServletRequestBuilder putRequest = put(String.format("%s/users/%s", baseUrl, 2))
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userPutDTO))
                .header("Authorization", getHeader(user));

        mockMvc.perform(putRequest).andExpect(status().isConflict());

    }

    @Test
    public void updateUser_inexistentUser_throws() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setStatus(UserStatus.OFFLINE);
        String jsonDTO = asJsonString(userPutDTO);

        given(userService.loadUserByUsername(Mockito.any())).willReturn(user);
        given(userService.getUserById(Mockito.any())).willReturn(user);
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user));

        MockHttpServletRequestBuilder putRequest = put(String.format("%s/users/%s", baseUrl, 1234))
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userPutDTO))
                .header("Authorization", getHeader(user));

        mockMvc.perform(putRequest).andExpect(status().isConflict());

    }

    @Test
    public void updateUser_userNameOrPasswordBlank_throws() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setStatus(UserStatus.OFFLINE);

        given(userService.loadUserByUsername(Mockito.any())).willReturn(user);
        given(userService.getUserById(Mockito.any())).willReturn(user);
        given(userService.idAndTokenMatch(Mockito.any(), Mockito.any())).willReturn(true);
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user));

        MockHttpServletRequestBuilder putRequest = put(String.format("%s/users/%s", baseUrl, user.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userPutDTO))
                .header("Authorization", getHeader(user));

        mockMvc.perform(putRequest).andExpect(status().isNoContent());

    }

    @Test
    public void updateUser_unauthorized_throws() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setStatus(UserStatus.OFFLINE);
        String jsonDTO = asJsonString(userPutDTO);

        given(userService.loadUserByUsername(Mockito.any())).willReturn(user);
        given(userService.getUserById(Mockito.any())).willReturn(user);
        given(userService.idAndTokenMatch(Mockito.any(), Mockito.any())).willReturn(true);
        given(userRepository.findById(Mockito.any())).willReturn(Optional.of(user));

        MockHttpServletRequestBuilder putRequest = put(String.format("%s/users/%s", baseUrl, user.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(userPutDTO));

        mockMvc.perform(putRequest).andExpect(status().isForbidden());

    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed Input will look like this: {"name": "Test User", "username":
     * "testUsername"}
     * 
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }

    private String getHeader(User user) {
        return String.format("Bearer %s", jwtUtil.generateToken(user));
    }
}