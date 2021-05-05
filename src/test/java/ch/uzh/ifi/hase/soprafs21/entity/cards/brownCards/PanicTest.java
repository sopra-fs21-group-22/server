package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Schofield;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

public class PanicTest {

    private List<Player> players;
    private List<Player> targets;
    private Panic panic = new Panic(Rank.SEVEN, Suit.HEARTS);

    @BeforeEach
    public void beforeEach() {
        PlayerTable table = new PlayerTable();
        Deck deck = new Deck();
        deck.setPlayCards(new ArrayList<>());
        table.setDiscardPile(deck);
        targets = new ArrayList<>();
        players = new ArrayList<>();
        Player oldPlayer = new Player();
        oldPlayer.setId(15L);
        players.add(oldPlayer);
        oldPlayer.setTable(table);
        oldPlayer.setOnFieldCards(new OnFieldCards());
        Hand hand = new Hand();
        hand.setPlayCards(new ArrayList<>());
        oldPlayer.setHand(hand);

        for (int i = 0; i < 6; i++) {
            Player newPlayer = new Player();
            newPlayer.setId(Long.valueOf(i));
            newPlayer.setOnFieldCards(new OnFieldCards());
            hand = new Hand();
            hand.setPlayCards(new ArrayList<>());
            newPlayer.setHand(hand);
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
        user.setDistanceDecreaseToOthers(1);
        assertTrue(panic.targetIsValid(user, user.getRightNeighbor().getRightNeighbor()));
    }

    @Test
    public void testInvalidTarget() {
        Player user = players.get(0);
        user.setRange(3);
        assertFalse(panic.targetIsValid(user, user.getLeftNeighbor().getLeftNeighbor()));
    }

    @Test
    public void testStealFieldCard() {
        PayLoadDTO payload = new PayLoadDTO();
        Player user = players.get(0);
        Player target = user.getRightNeighbor();
        BlueCard toBeStolenCard = new Schofield(Rank.ACE, Suit.SPADES);
        toBeStolenCard.setId(100L);
        target.getOnFieldCards().addOnFieldCard(toBeStolenCard);
        payload.setTargetCardId(100L);

        panic.use(user, target, payload);
        assertTrue(user.getHand().getPlayCards().contains(toBeStolenCard));
        assertFalse(target.getOnFieldCards().contains(toBeStolenCard));
    }

    @Test
    public void testStealHandCard() {
        PayLoadDTO payload = new PayLoadDTO();
        Player user = players.get(0);
        Player target = user.getRightNeighbor();
        BlueCard toBeStolenCard = new Schofield(Rank.ACE, Suit.SPADES);
        target.getHand().addCard(toBeStolenCard);

        panic.use(user, target, payload);

        assertTrue(user.getHand().getPlayCards().contains(toBeStolenCard));
        assertFalse(target.getHand().getPlayCards().contains(toBeStolenCard));
    }
}
