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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * User Service This class is the "worker" and responsible for all functionality
 * related to the user (e.g., it creates, modifies, deletes, finds). The result
 * will be passed back to the caller.
 */
@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private final JwtUtil jwtUtil;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException(id.toString());
        }

    }

    public User getDefaultUser() {
        Optional<User> defaultUserOpt = this.userRepository.findById(2718L);
        if (defaultUserOpt.isPresent()) {
            return defaultUserOpt.get();
        }
        User defaultUser = new User();
        defaultUser.setId(2718L);
        defaultUser.setUsername("<left>");
        defaultUser.setPassword("aj29308fjaöslkdfja9283fjölkjasdf9a28jflkdjaj293fjsdkl");
        return createUser(defaultUser);
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    /**
     * checks for fields set in param and creates a user with them
     * 
     * @param newUser
     * @return newly created User
     */
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
        newUser.setStatus(UserStatus.OFFLINE);

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

    public User updateUser(Long userId, User newUser) {
        User currUser = userRepository.findById(userId).get();

        if (newUser.getUsername() != null && newUser.getUsername().length() > 0) {
            currUser.setUsername(newUser.getUsername());
        }

        if (newUser.getBirthday() != null) {
            currUser.setBirthday(newUser.getBirthday());
        }

        if (newUser.getStatus() != null) {
            currUser.setStatus(newUser.getStatus());
        }

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
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean idAndTokenMatch(Long id, String token) {
        User idUser = getUserById(id);
        String tokenUsername = jwtUtil.extractUsername(token);

        return idUser.getUsername().equals(tokenUsername);
    }

    public void throwIfNotIdAndTokenMatch(Long id, String auth) {
        if (!this.idAndTokenMatch(id, auth.substring(7))) {
            throw new IllegalArgumentException("Token and user do not match!");
        }
    }

    public Long getIdByToken(String auth) {
        return loadUserByUsername(jwtUtil.extractUsername(auth.substring(7))).getId();
    }

    public void updateUserStatus(Long id, UserStatus status) {
        User user = getUserById(id);
        user.setStatus(status);
    }
}
