package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

//import org.hibernate.mapping.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

@SpringBootTest
public class PlayerServiceTest {

    @Autowired
    PlayerTableService playerTableService;

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerRepository playerRepository;

    @AfterEach
    public void afterEach() {
        playerRepository.deleteAll();
        playerRepository.flush();
    }

    @Transactional
    @Test
    public void attackAllTest() {
        List<Player> players = new ArrayList<Player>();
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(2L);
        Player player3 = new Player();
        player3.setId(3L);

        players.add(player1);
        players.add(player2);
        players.add(player3);

        int[] bullets = { player1.getBullets() - 1, player2.getBullets() - 1, player3.getBullets() - 1 };

        for (int i = 1; i < players.size(); i++) {
            playerService.attackPlayer(player1, players.get(i));
        }

        for (Integer i = 1; i < players.size(); i++) {
            assertEquals(bullets[i], players.get(i).getBullets());
        }
    }
}
