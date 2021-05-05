package ch.uzh.ifi.hase.soprafs21.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.HandRepository;
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
    HandRepository handRepository;


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

    @Transactional
    @Test
    public void setPlayerAsReady_gameStarts() {
        PlayerTable table = playerTableService.addPlayer(users.get(8).getId());
        playerTableService.setPlayerAsReady(table.getId(), users.get(8).getId(), true);
        for (int i = 0; i < 3; i++) {
            User user = users.get(i);
            table = playerTableService.addPlayer(user.getId());
            playerTableService.setPlayerAsReady(table.getId(), user.getId(), true);
        }
        PlayerTable actualTable = playerTableRepository.getOne(table.getId());
        assertNotNull(actualTable.getPlayerOnTurn());
    }

    @Transactional
    @Test
    public void setPlayerAsReady_gameStartsNot() {
        PlayerTable table = playerTableService.addPlayer(users.get(8).getId());
        for (int i = 0; i < 3; i++) {
            User user = users.get(i);
            table = playerTableService.addPlayer(user.getId());
            playerTableService.setPlayerAsReady(table.getId(), user.getId(), true);
        }
        assertNull(table.getPlayerOnTurn());
    }

    @Transactional
    @Test
    public void setPlayerAsReady_RolesGetAssignedCorrectly4Players() {
        PlayerTable table = playerTableService.addPlayer(users.get(8).getId());
        playerTableService.setPlayerAsReady(table.getId(), users.get(8).getId(), true);
        for (int i = 0; i < 3; i++) {
            User user = users.get(i);
            table = playerTableService.addPlayer(user.getId());
            playerTableService.setPlayerAsReady(table.getId(), user.getId(), true);
        }
        ArrayList<GameRole> roles = new ArrayList<>();
        for (Player player : playerTableRepository.getOne(table.getId()).getPlayers()) {
            roles.add(player.getGameRole());
        }
        List<GameRole> expectedRoles = new ArrayList<>();
        expectedRoles.add(GameRole.SHERIFF);
        expectedRoles.add(GameRole.OUTLAW);
        expectedRoles.add(GameRole.RENEGADE);

        assertTrue(roles.containsAll(expectedRoles));
        assertTrue(roles.size() == 4);
    }

    @Transactional
    @Test
    public void moreThan7PlayersJoin() {
        PlayerTable table = playerTableService.addPlayer(users.get(9).getId());
        playerTableService.setPlayerAsReady(table.getId(), users.get(9).getId(), true);
        for (int i = 0; i < 4; i++) {
            User user = users.get(i);
            table = playerTableService.addPlayer(user.getId());
            playerTableService.setPlayerAsReady(table.getId(), user.getId(), true);
        }
        assertEquals(1, table.getPlayers().size());
    }

   /*  @Test
    @Transactional
    public void newPlayersGetCorrectLifePoints() {
        PlayerTable table;
        table = playerTableService.addPlayer(users.get(0).getId());
        for (int i = 1; i < 7; i++) {
            playerTableService.addPlayer(users.get(i).getId());
        }

       Integer sum = 0;
       for (int i = 0; i < 7; i++) {
            sum = sum + table.getPlayers().get(i).getBullets();
        }

        assertEquals(26,sum);
    }

    @Test
    @Transactional
    public void oneOfEveryCharacterExists() {
        PlayerTable table;
        table = playerTableService.addPlayer(users.get(0).getId());
        for (int i = 1; i < 7; i++) {
            playerTableService.addPlayer(users.get(i).getId());
        }

       Integer count = 0;
       for (int i = 0; i < 7; i++) {
            if (table.getPlayers().get(i).getCharacterCard().getName().equals("Willy The Kid")) {
                count++;
            }
        }

        assertEquals(1,count);

    count = 0;
       for (int i = 0; i < 7; i++) {
            if (table.getPlayers().get(i).getCharacterCard().getName().equals("Rose Doolan")) {
                count++;
            }
        }

        assertEquals(1,count);

       count = 0;
       for (int i = 0; i < 7; i++) {
            if (table.getPlayers().get(i).getCharacterCard().getName().equals("Paul Regret")) {                 //TESTING
                count++;
            }
        }

        assertEquals(1,count);

       count = 0;
       for (int i = 0; i < 7; i++) {
            if (table.getPlayers().get(i).getCharacterCard().getName().equals("Jourdonnais")) {
                count++;
            }
        }

        assertEquals(1,count);

        count = 0;
       for (int i = 0; i < 7; i++) {
            if (table.getPlayers().get(i).getCharacterCard().getName().equals("Bart Cassidy")) {
                count++;
            }
        }

        assertEquals(1,count);

        count = 0;
       for (int i = 0; i < 7; i++) {
            if (table.getPlayers().get(i).getCharacterCard().getName().equals("Suzy Lafayette")) {
                count++;
            }
        }

        assertEquals(1,count);

        count = 0;
       for (int i = 0; i < 7; i++) {
            if (table.getPlayers().get(i).getCharacterCard().getName().equals("El Gringo")) {
                count++;
            }
        }

        assertEquals(1,count);
    }
     */

    // @Transactional
    // @Test
    // public void setPlayerAsReady_tablePositionsGetAssigned() {
    // PlayerTable table = playerTableService.addPlayer(users.get(8).getId());
    // playerTableService.setPlayerAsReady(table.getId(), users.get(8).getId(),
    // true);
    // for (int i = 0; i < 3; i++) {
    // User user = users.get(i);
    // table = playerTableService.addPlayer(user.getId());
    // playerTableService.setPlayerAsReady(table.getId(), user.getId(), true);
    // }
    // List<Integer> tablePositions = new ArrayList<>();
    // for (Player player : table.getPlayers()) {
    // tablePositions.add(player.getTablePosition());
    // }
    // List<Integer> expectedPositionsUnordered = new ArrayList<>();
    // for (int i = 0; i < 4; i++) {
    // expectedPositionsUnordered.add(i);
    // }

    // assertTrue(tablePositions.containsAll(expectedPositionsUnordered));
    // }

    // @Test
    // @Transactional
    // public void playersInRange() {
    // PlayerTable table = playerTableService.addPlayer(users.get(8).getId());
    // playerTableService.setPlayerAsReady(table.getId(), users.get(8).getId(),
    // true);
    // for (int i = 0; i < 3; i++) {
    // User user = users.get(i);
    // table = playerTableService.addPlayer(user.getId());
    // playerTableService.setPlayerAsReady(table.getId(), user.getId(), true);
    // }

    // Player attackingPlayer = table.getPlayers().get(0);

    // List<Player> playersInRange = playerTableService.getPlayersInRangeOf(table,
    // attackingPlayer.getId());
    // for (Player playerInRange : playersInRange) {
    // int atcPos = attackingPlayer.getTablePosition();
    // int inRangePos = playerInRange.getTablePosition();
    // int distance = Math.abs(atcPos - inRangePos);
    // assertTrue(distance == 1 || distance == table.getPlayers().size() - 1);
    // }
    // }
}
