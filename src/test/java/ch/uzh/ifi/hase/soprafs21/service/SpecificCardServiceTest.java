package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.BrownCard;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

//import org.hibernate.mapping.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
public class SpecificCardServiceTest {
    
    @Autowired
    PlayerService playerService;

    @Autowired
    SpecificCardService specificCardService;

    @Autowired
    PlayerRepository playerRepository;

    @AfterEach
    public void afterEach() {
        playerRepository.deleteAll();
        playerRepository.flush();
    }

    @Transactional
    @Test
    public void gatlingTest() {
        PlayerTable table = new PlayerTable();
        PlayCard playCard = new BrownCard(Card.GATLING, Rank.ACE, Suit.CLUBS );
        List<Player> players = new ArrayList<Player>();
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(2L);
        Player player3 = new Player();
        player3.setId(3L);
        Player activePlayer = new Player();
        activePlayer.setId(4L);

        players.add(player1);
        players.add(player2);
        players.add(player3);

        activePlayer.setBullets(0);
        Assertions.assertThrows( IllegalArgumentException.class, () -> {specificCardService.use(table, playCard, activePlayer, players);});

        activePlayer.setBullets(4);
        players.get(1).setBullets(0);
        Assertions.assertThrows( IllegalArgumentException.class, () -> {specificCardService.use(table, playCard, activePlayer, players);});

        players.get(1).setBullets(4);
        specificCardService.use(table, playCard, activePlayer, players);
        
        for (Integer i = 0; i < players.size(); i++) {
            assertEquals(players.get(i).getBullets(), 3);
        }
    }

    @Transactional
    @Test
    public void beerTest() {
        PlayerTable table = new PlayerTable();
        PlayCard playCard = new BrownCard(Card.BEER, Rank.ACE, Suit.CLUBS );
        List<Player> players = new ArrayList<Player>();
        Player activePlayer = new Player();
        activePlayer.setId(1L);

        activePlayer.setBullets(0);
        Assertions.assertThrows( IllegalArgumentException.class, () -> {specificCardService.use(table, playCard, activePlayer, players);});

        activePlayer.setBullets(4);
        Assertions.assertThrows( UnsupportedOperationException.class, () -> {specificCardService.use(table, playCard, activePlayer, players);});

        activePlayer.setBullets(3);
        specificCardService.use(table, playCard, activePlayer, players);
        
        assertEquals(activePlayer.getBullets(), 4);
    }

    @Transactional
    @Test
    public void saloonTest() {
        PlayerTable table = new PlayerTable();
        PlayCard playCard = new BrownCard(Card.SALOON, Rank.ACE, Suit.CLUBS );
        List<Player> players = new ArrayList<Player>();
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(2L);
        Player player3 = new Player();
        player3.setId(3L);
        Player activePlayer = new Player();
        activePlayer.setId(4L);

        players.add(player1);
        players.add(player2);
        players.add(player3);

        activePlayer.setBullets(0);
        Assertions.assertThrows( IllegalArgumentException.class, () -> {specificCardService.use(table, playCard, activePlayer, players);});

        activePlayer.setBullets(4);
        Assertions.assertThrows( UnsupportedOperationException.class, () -> {specificCardService.use(table, playCard, activePlayer, players);});

        activePlayer.setBullets(3);
        specificCardService.use(table, playCard, activePlayer, players);
        
        assertEquals(activePlayer.getBullets(), 4);
    }

    @Transactional
    @Test
    public void bangTest() {
        PlayerTable table = new PlayerTable();
        PlayCard playCard = new BrownCard(Card.BANG, Rank.ACE, Suit.CLUBS );
        List<Player> players = new ArrayList<Player>();
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(2L);
        Player player3 = new Player();
        player3.setId(3L);
        Player activePlayer = new Player();
        activePlayer.setId(4L);
        
        playerRepository.save(player1);
        playerRepository.save(player2);
        playerRepository.save(player3);
        playerRepository.save(activePlayer);

        players.add(player1);
        players.add(player2);

        Assertions.assertThrows( IllegalArgumentException.class, () -> {specificCardService.use(table, playCard, activePlayer, players);});

        players.remove(1);

        activePlayer.setBullets(0);
        Assertions.assertThrows( IllegalArgumentException.class, () -> {specificCardService.use(table, playCard, activePlayer, players);});
        activePlayer.setBullets(4);

        players.get(0).setBullets(0);
        Assertions.assertThrows( IllegalArgumentException.class, () -> {specificCardService.use(table, playCard, activePlayer, players);});
        players.get(0).setBullets(4);

        activePlayer.setStillPlayableBangsThisRound(1);
        activePlayer.setRightNeighbor(players.get(0));
        activePlayer.setLeftNeighbor(player3);
        player3.setLeftNeighbor(players.get(0));

        specificCardService.use(table, playCard, activePlayer, players);
        
        assertEquals(players.get(0).getBullets(), 3);
    }

}