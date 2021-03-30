package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser_validInputs_success() {
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("password");

        // when
        User createdUser = userService.createUser(testUser);

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        // testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();

        // change the name but forget about the username
        // testUser2.setName("testName2");
        testUser2.setUsername("testUsername");
        testUser2.setPassword("password");

        // check that an error is thrown
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(testUser2));
    }

    @Test
    public void createUser_takenUsername_throwsError() {
        User user1 = new User();
        user1.setUsername("Flegel");
        user1.setPassword("password");
        userService.createUser(user1);

        User user2 = new User();
        user2.setUsername("Flegel");
        user2.setPassword("asdf");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user2));

    }

    @Test
    public void createUser_blankOrNullCredentials_throwsError() {
        User user = new User();
        user.setUsername("");
        user.setPassword("password");

        User user2 = new User();
        user.setUsername("username");
        user.setPassword("");

        User user3 = new User();
        user.setUsername("username");
        user.setPassword(null);

        User user4 = new User();
        user.setUsername(null);
        user.setPassword("password");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user2);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user3);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user4);
        });

    }

    @Test
    public void getUserById_success() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user = userService.createUser(user);
        User userById = userService.getUserById(user.getId());
        assertEquals(user.getUsername(), userById.getUsername());
    }

    @Test
    public void getUserById_userNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(Long.valueOf(12));
        });
    }

    @Transactional
    @Test
    public void updateUser_success() {
        User user = new User();
        user.setUsername("Flegel");
        user.setPassword("password");
        user.setBirthday(new Date(1000 * 60 * 60 * 24 * 365 * 12));
        userService.createUser(user);

        User updateUser = new User();
        Date newBirthday = new Date(1000 * 60 * 60 * 24 * 365 * 10);
        updateUser.setUsername("Eren");
        updateUser.setBirthday(newBirthday);
        userService.updateUser(user.getId(), updateUser);

        User updatedUser = userRepository.getOne(user.getId());
        assertEquals(user.getUsername(), updatedUser.getUsername());
        assertEquals(newBirthday, updatedUser.getBirthday());
    }

    @Test
    public void updateUser_blankUsername_doesNothing() {
        User user = new User();
        user.setUsername("Flegel");
        user.setPassword("password");
        User savedUser = userService.createUser(user);

        User updateUser = new User();
        updateUser.setUsername("");
        updateUser.setPassword("password");

        Long id = savedUser.getId();

        userService.updateUser(id, updateUser);

        User updatedUser = userService.getUserById(id);
        assertEquals("Flegel", updatedUser.getUsername());

    }

    @Test
    public void updateUser_takenUsername_throws() {
        User user = new User();
        user.setUsername("Flegel");
        user.setPassword("password");
        User savedUser = userService.createUser(user);

        User user2 = new User();
        user2.setUsername("Levi");
        user2.setPassword("password");
        userService.createUser(user2);

        User updateUser = new User();
        updateUser.setUsername("Levi");

        Long id = savedUser.getId();

        assertThrows(DataIntegrityViolationException.class, () -> {
            userService.updateUser(id, updateUser);
        });
    }

}
