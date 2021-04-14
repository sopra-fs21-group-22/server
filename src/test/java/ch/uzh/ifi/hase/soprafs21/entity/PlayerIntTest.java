package ch.uzh.ifi.hase.soprafs21.entity;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.service.PlayerTableService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

@SpringBootTest
public class PlayerIntTest {

    private PlayerTable table;

    private List<User> users;

    @Autowired
    private PlayerTableService playerTableService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerTableRepository playerTableRepository;

    @BeforeEach
    public void beforeEach() {
        users = new ArrayList<>();
        String[] usernames = { "hijikata", "ogata", "tsukishima", "sugimoto", "asiripa", "shiraishi", "ushiyama",
                "inudou", "tanigaki", "nihei" };
        for (int i = 0; i < 4; i++) {
            User newUser = new User();
            newUser.setUsername(usernames[i]);
            newUser.setPassword("password");
            users.add(userService.createUser(newUser));
        }
        for (User user : users) {
            table = playerTableService.addPlayer(user.getId());
            playerTableService.setPlayerAsReady(table.getId(), user.getId(), true);
        }
        // table = playerTableService.getPlayerTableById(table.getId());

    }

    @AfterEach
    public void afterEach() {
        playerTableRepository.deleteAll();
        playerTableRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    @Transactional
    public void testPlayerInRange_success() {
        User user = users.get(0);
        Player player = table.getPlayerById(user.getId()).get();
        List<Player> playersInRange = table.getPlayersInRangeOf(user.getId());
        assertTrue(playersInRange.contains(player.getLeftNeighbor()));
        assertTrue(playersInRange.contains(player.getRightNeighbor()));
    }

    // @Test
    // @Transactional
    // public void testPlayerInRange_failure() {

    // }

}
