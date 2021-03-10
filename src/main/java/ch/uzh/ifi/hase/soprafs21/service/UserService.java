package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.UserNotFoundException;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

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
        long idAsNumber = Long.parseLong(id);
        return this.userRepository.getOne(idAsNumber);
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    // public String login(User user) {

    // }

    public User createUser(User newUser) {
        Date date = new Date(System.currentTimeMillis());
        newUser.setCreationDate(date);
        newUser.setStatus(UserStatus.ONLINE);

        if (userExists(newUser)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken!");
        }

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User updateUser(String id, User newUser) {
        User currUser = userRepository.findById(Long.parseLong(id)).get();
        currUser.setUsername(newUser.getUsername());
        currUser.setBirthday(newUser.getBirthday());
        return userRepository.save(currUser);
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
}
