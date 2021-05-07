package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

public class IndiansTest {

    private Indians indians = new Indians(Rank.ACE, Suit.CLUBS);
    private List<Player> players;
    private Player user;

    @BeforeEach
    public void beforeEach() {

        players = new ArrayList<>();

        Deck deck = new Deck();
        deck.setPlayCards(new ArrayList<>());

        PlayerTable table = new PlayerTable();
        table.setDiscardPile(deck);
        table.setPlayers(players);

        Player oldPlayer = new Player();
        oldPlayer.setId(15L);
        oldPlayer.setTable(table);
        oldPlayer.setOnFieldCards(new OnFieldCards());
        players.add(oldPlayer);

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
    public void testTargetIsValid() {
        assertTrue(indians.targetIsValid(null, null));
    }

    @Test
    public void bangHolderNoDMG() {
        user = players.get(0);
        Player targetWithBang = user.getRightNeighbor();
        targetWithBang.getHand().getPlayCards().add(new Bang(Rank.ACE, Suit.SPADES));
        int expectedLives = targetWithBang.getBullets();

        indians.use(user, user, null);
        assertEquals(expectedLives, targetWithBang.getBullets());
    }

    @Test
    public void noBangHolderTakesDMG() {
        user = players.get(0);
        Player targetWithoutBang = user.getLeftNeighbor();
        int expectedLives = targetWithoutBang.getBullets() - 1;

        indians.use(user, user, null);
        assertEquals(expectedLives, targetWithoutBang.getBullets());
    }

    @Test
    public void bangHolderLosesBang() {
        user = players.get(0);
        Player targetWithBang = user.getRightNeighbor();
        targetWithBang.getHand().getPlayCards().add(new Bang(Rank.ACE, Suit.SPADES));
        indians.use(user, user, null);
        assertEquals(0, targetWithBang.getHand().getPlayCards().size());
    }

    @Test
    public void doesntHitUser() {
        user = players.get(0);
        int expectedLives = user.getBullets();

        indians.use(user, user, null);
        assertEquals(expectedLives, user.getBullets());
    }

}
