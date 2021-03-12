package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.util.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

/**
 * User Service This class is the "worker" and responsible for all functionality
 * related to the user (e.g., it creates, modifies, deletes, finds). The result
 * will be passed back to the caller.
 */
@Service
@Transactional
public class UserService implements UserDetailsService {

    private final JwtUtil jwtUtil = new JwtUtil();

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getUserById(String id) {
        Long idAsNumber;
        try {
            idAsNumber = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new UserNotFoundException(id);
        }
        Optional<User> user = this.userRepository.findById(idAsNumber);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException(id);
        }

    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public User createUser(User newUser) {
        // check for null
        if (newUser.getUsername() == null || newUser.getPassword() == null) {
            throw new IllegalArgumentException("Username/password can't be null!");
        }

        // check if username blank
        if (newUser.getUsername().length() < 1 || newUser.getPassword().length() < 1) {
            throw new IllegalArgumentException("Username/password can't be blank!");
        }

        Date date = new Date(System.currentTimeMillis());
        newUser.setCreationDate(date);
        newUser.setStatus(UserStatus.ONLINE);

        if (userExists(newUser)) {
            throw new IllegalArgumentException("Username already taken!");
        }

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User updateUser(String userId, User newUser) {

        if (newUser.getUsername() == null) {
            throw new IllegalArgumentException("Username can't be null!");
        }

        // check if new username is blank
        if (newUser.getUsername().length() < 1) {
            throw new IllegalArgumentException("Username can't be blank!");
        }

        User currUser = userRepository.findById(Long.parseLong(userId)).get();

        // check if username changed
        if (!currUser.getUsername().equals(newUser.getUsername())) {
            // check if new username already taken
            List<User> users = getUsers();
            for (User user : users) {
                if (newUser.getUsername().equals(user.getUsername())) {
                    throw new IllegalArgumentException("New Username already taken!");
                }
            }
        }

        currUser.setUsername(newUser.getUsername());
        currUser.setBirthday(newUser.getBirthday());

        User user = userRepository.save(currUser);
        userRepository.flush();
        return user;

    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name defined in the User entity. The method will do nothing
     * if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private Boolean userExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        return userByUsername != null;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean idAndTokenMatch(String id, String token) {
        User idUser = getUserById(id);
        String tokenUsername = jwtUtil.extractUsername(token);
        log.error(String.format("Token Username: %s", tokenUsername));
        log.error(String.format("Id User: %s", idUser.getUsername()));

        return idUser.getUsername().equals(tokenUsername);
    }
}
