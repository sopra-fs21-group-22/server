package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

import org.hibernate.mapping.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    private User otherTestUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        testUser.setStatus(UserStatus.OFFLINE);

        otherTestUser = new User();
        otherTestUser.setId(2L);
        otherTestUser.setUsername("Fred");
        otherTestUser.setPassword("password");
        otherTestUser.setStatus(UserStatus.ONLINE);

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void updateUser_validInput_success() {
        userService.createUser(testUser);

        User updateUser = new User();
        updateUser.setUsername("newUsername");
        Date newBirthday = new Date(System.currentTimeMillis());
        updateUser.setBirthday(newBirthday);
        updateUser.setStatus(UserStatus.ONLINE);
        Optional<User> optUser = Optional.of(testUser);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(optUser);

        User updatedUser = userService.updateUser(testUser.getId(), updateUser);

        Mockito.verify(userRepository, Mockito.times(2)).save(Mockito.any());

        assertEquals(testUser.getId(), updatedUser.getId());
        assertEquals("newUsername", updatedUser.getUsername());
        assertEquals(newBirthday, updatedUser.getBirthday());
    }

    // commented out since error is thrown when saving to db which is happening in
    // integration testing and not here
    // @Test
    // public void updateUser_usernameTaken_throws() {
    // userService.createUser(testUser);
    // userService.createUser(otherTestUser);

    // ArrayList<User> allUsers = new ArrayList<User>();
    // allUsers.add(testUser);
    // allUsers.add(otherTestUser);

    // User updateUser = new User();
    // updateUser.setUsername("Fred");
    // Date newBirthday = new Date(System.currentTimeMillis());
    // updateUser.setBirthday(newBirthday);
    // updateUser.setStatus(UserStatus.ONLINE);
    // Optional<User> optUser = Optional.of(testUser);

    // Mockito.when(userRepository.findById(Mockito.any())).thenReturn(optUser);

    // Mockito.when(userRepository.findAll()).thenReturn(allUsers);

    // // userService.updateUser(testUser.getId(), updateUser);

    // assertThrows(IllegalArgumentException.class, () ->
    // userService.updateUser(testUser.getId(), updateUser));

    // Mockito.verify(userRepository, Mockito.times(2)).save(Mockito.any());

    // }

}
