package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Schofield;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

public class CatBalouTest {

    private List<Player> players;
    private CatBalou catBalou = new CatBalou(Rank.SEVEN, Suit.HEARTS);
    private PlayerTable table = new PlayerTable();

    @BeforeEach
    public void beforeEach() {
        Deck deck = new Deck();
        table.setDeck(deck);

        // create a game with 7 players and their Hand & onField Cards
        players = new ArrayList<>();
        Player oldPlayer = new Player();
        User user = new User();
        user.setUsername("Ada");
        oldPlayer.setUser(user);
        oldPlayer.setId(15L);
        oldPlayer.setTable(table);
        table.setPlayerOnTurn(oldPlayer); // players.get(0) onTurn
        table.setDiscardPile(new Deck());
        players.add(oldPlayer);
        oldPlayer.setOnFieldCards(new OnFieldCards());
        oldPlayer.setHand(new Hand());

        for (int i = 0; i < 6; i++) {
            Player newPlayer = new Player();
            user = new User();
            user.setUsername("Ada");
            newPlayer.setUser(user);
            newPlayer.setId(Long.valueOf(i));
            newPlayer.setOnFieldCards(new OnFieldCards());
            newPlayer.setHand(new Hand());
            newPlayer.setTable(table);
            players.add(newPlayer);
            newPlayer.setRightNeighbor(oldPlayer);
            oldPlayer.setLeftNeighbor(newPlayer);
            oldPlayer = newPlayer;
        }
        Player firstPlayer = players.get(0);
        Player lastPlayer = players.get(players.size() - 1);
        firstPlayer.setRightNeighbor(lastPlayer);
        lastPlayer.setLeftNeighbor(firstPlayer);
    }

    @Test
    public void testValidTarget() {
        Player user = players.get(0);
        assertTrue(catBalou.targetIsValid(user, user.getRightNeighbor().getRightNeighbor().getRightNeighbor()));
    }

    @Test
    public void testInvalidTarget() {
        Player user = players.get(0);
        user.setRange(3);
        assertFalse(catBalou.targetIsValid(user, user));
    }

    @Test
    public void testStealFieldCard() {
        PayLoadDTO payload = new PayLoadDTO();
        Player user = players.get(0);
        Player target = user.getRightNeighbor();
        BlueCard toBeDiscardedCard = new Schofield(Rank.ACE, Suit.SPADES);
        toBeDiscardedCard.setId(100L);
        target.getOnFieldCards().addOnFieldCard(toBeDiscardedCard);
        payload.setTargetCardId(100L);

        catBalou.use(user, target, payload);
        assertTrue(user.getTable().getDiscardPile().getPlayCards().contains(toBeDiscardedCard));
        assertFalse(target.getOnFieldCards().contains(toBeDiscardedCard));
    }

    @Test
    public void testStealHandCard() {
        PayLoadDTO payload = new PayLoadDTO();
        Player user = players.get(0);
        Player target = user.getRightNeighbor();
        BlueCard toBeDiscardedCard = new Schofield(Rank.ACE, Suit.SPADES);
        target.getHand().addCard(toBeDiscardedCard);

        catBalou.use(user, target, payload);

        assertTrue(user.getTable().getDiscardPile().getPlayCards().contains(toBeDiscardedCard));
        assertFalse(target.getHand().getPlayCards().contains(toBeDiscardedCard));
    }
}
