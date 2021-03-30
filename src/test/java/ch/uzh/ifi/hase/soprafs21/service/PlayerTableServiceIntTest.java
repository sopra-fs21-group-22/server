package ch.uzh.ifi.hase.soprafs21.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

@SpringBootTest
public class PlayerTableServiceIntTest {

    @Autowired
    PlayerTableService playerTableService;

    @Autowired
    PlayerTableRepository playerTableRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private List<User> users;

    @BeforeEach
    public void beforeEach() {
        users = new ArrayList<User>();
        String[] usernames = { "hijikata", "ogata", "tsukishima", "sugimoto", "asiripa", "shiraishi", "ushiyama",
                "inudou", "tanigaki", "nihei" };
        for (int i = 0; i < 10; i++) {
            User newUser = new User();
            newUser.setUsername(usernames[i]);
            newUser.setPassword("password");
            users.add(userService.createUser(newUser));
        }
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
    public void addUser_validInputs_success() {
        PlayerTable newTable = playerTableService.addPlayer(users.get(0).getId());
        PlayerTable actualTable = playerTableRepository.getOne(newTable.getId());

        Integer expectedLength = 1;
        Integer actualLength = actualTable.getPlayers().size();
        assertEquals(expectedLength, actualLength);

        String actualUsername = actualTable.getPlayers().get(0).getUser().getUsername();
        String expectedUsername = users.get(0).getUsername();
        assertEquals(expectedUsername, actualUsername);

    }

    @Transactional
    @Test
    public void fillTable_success() {
        PlayerTable table1, table2, actualTable1, actualTable2;
        table1 = playerTableService.addPlayer(users.get(0).getId());
        for (int i = 1; i < 7; i++) {
            playerTableService.addPlayer(users.get(i).getId());
        }
        table2 = playerTableService.addPlayer(users.get(7).getId());

        for (int i = 8; i < 10; i++) {
            playerTableService.addPlayer(users.get(i).getId());
        }

        actualTable1 = playerTableRepository.getOne(table1.getId());
        actualTable2 = playerTableRepository.getOne(table2.getId());

        Integer actualLengthT1 = actualTable1.getPlayers().size();
        Integer expectedLengthT1 = 7;
        assertEquals(expectedLengthT1, actualLengthT1);

        Integer actualLengthT2 = actualTable2.getPlayers().size();
        Integer expectedLengthT2 = 3;
        assertEquals(expectedLengthT2, actualLengthT2);

    }

    @Test
    @Transactional
    public void addUserAlreadyInGameToGame_fail() {
        playerTableService.addPlayer(users.get(0).getId());
        assertThrows(IllegalArgumentException.class, () -> {
            playerTableService.addPlayer(users.get(0).getId());
        });
    }

    @Test
    @Transactional
    public void startGameWithLT4Players() {
        PlayerTable table = playerTableService.addPlayer(users.get(0).getId());
        assertThrows(IllegalArgumentException.class, () -> {
            playerTableService.startGame(table.getId());
        });

        playerTableService.addPlayer(users.get(1).getId());
        playerTableService.addPlayer(users.get(2).getId());
        assertThrows(IllegalArgumentException.class, () -> {
            playerTableService.startGame(table.getId());
        });

        playerTableService.addPlayer(users.get(3).getId());
        playerTableService.startGame(table.getId());
        Boolean expected = true;
        Boolean actual = playerTableRepository.getOne(table.getId()).getGameHasStarted();
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void joinStartedGame_fails() {
        PlayerTable table = playerTableService.addPlayer(users.get(0).getId());
        for (int i = 1; i < 4; i++) {
            playerTableService.addPlayer(users.get(i).getId());
        }
        playerTableService.startGame(table.getId());
        PlayerTable newTable = playerTableService.addPlayer(users.get(6).getId());
        assertEquals(1, newTable.getPlayers().size());
    }
}
