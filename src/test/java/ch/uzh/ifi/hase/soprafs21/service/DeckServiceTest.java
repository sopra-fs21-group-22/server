package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
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
public class DeckServiceTest {

    @Autowired
    DeckService deckService;

    @Autowired
    DeckRepository deckRepository;

    @BeforeEach
    public void beforeEach() {
        deckRepository.deleteAll();
        deckRepository.flush();
    }

    @AfterEach
    public void afterEach() {
        deckRepository.deleteAll();
        deckRepository.flush();
    }

    @Transactional
    @Test
    public void deckCreationSuccess() {

        Deck deck = new Deck();

        deckService.fill(deck);

        List<PlayCard> testList = deck.getPlayCards();

        for (int i = 0; i < testList.size(); i++) {
            System.out.print(testList.get(i));
        }

    }

    @Transactional
    @Test
    public void deckShuffleSuccess() {

        PlayerTable table = new PlayerTable();
        Player player = new Player();
        Hand hand = new Hand();
        Deck deck = new Deck();
        Deck discardPile = new Deck();
        List<Player> players = new ArrayList<Player>();

        table.setDeck(deck);
        table.setDiscardPile(discardPile);
        player.setHand(hand);
        table.setPlayers(players);

        deckService.fill(table.getDiscardPile());

        List<PlayCard> testList = table.getDiscardPile().getPlayCards();

        for (int i = 0; i < testList.size(); i++) {
            System.out.print(testList.get(i));
        }

        deckService.shuffle(table);

        List<PlayCard> testList2 = table.getDiscardPile().getPlayCards();

        for (int i = 0; i < testList2.size(); i++) {
            System.out.print(testList2.get(i));
        }

        List<PlayCard> testList3 = table.getDeck().getPlayCards();

        for (int i = 0; i < testList3.size(); i++) {
            System.out.print(testList3.get(i));
        }
    }
}
