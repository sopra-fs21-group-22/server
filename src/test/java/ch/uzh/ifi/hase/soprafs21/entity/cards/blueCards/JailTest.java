package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JailTest {
    private List<Player> players;

    private BlueCard jail = new Jail(Rank.SEVEN, Suit.HEARTS);

    @BeforeEach
    public void beforeEach() {
        jail.setId(33L);

        // create a table with a deck and a discard pile
        PlayerTable table = new PlayerTable();
        Deck deck = new Deck();
        table.setDeck(deck);
        ArrayList<PlayCard> playCards = new ArrayList<>();
        Bang bang = new Bang(Rank.ACE, Suit.HEARTS);
        playCards.add(bang);
        deck.setPlayCards(playCards);
        table.setDiscardPile(deck);

        // create a game with 7 players and their Hand & onField Cards
        players = new ArrayList<>();
        Player oldPlayer = new Player();
        User user = new User();
        user.setUsername("Ada");
        oldPlayer.setUser(user);
        oldPlayer.setId(15L);
        oldPlayer.setTable(table);
        table.setPlayerOnTurn(oldPlayer); // players.get(0) onTurn
        players.add(oldPlayer);
        oldPlayer.setOnFieldCards(new OnFieldCards());
        oldPlayer.setHand(new Hand());

        for (int i = 0; i < 6; i++) {
            Player newPlayer = new Player();
            user = new User();
            user.setUsername("Beta");
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
    public void testOnPlacement() {
        Player playerWithJail = players.get(0);
        Player target = players.get(1);
        jail.use(playerWithJail, target, null);
        assertTrue(target.getOnFieldCards().contains(jail));
    }

    @Test
    public void testOnPlacementWithSheriff() {
        Player playerWithJail = players.get(0);
        Player target = players.get(1);
        target.setGameRole(GameRole.SHERIFF);
        assertThrows(GameLogicException.class, () -> jail.use(playerWithJail, target, null));
    }

    @Test
    public void testEnterPrisonTwice() {
        Player playerWithJail = players.get(1);
        Player playerInJail = players.get(0);
        jail.use(playerWithJail, playerInJail, null);
        assertThrows(GameLogicException.class, () -> jail.use(playerWithJail, playerInJail, null));
    }

    @Test
    public void testOnTurnStartWithHearts() {
        Player player = players.get(0); // on turn
        Player playerInJail = players.get(1);
        jail.use(player, playerInJail, null);
        player.getTable().setPlayerOnTurn(playerInJail);

        jail.onTurnStart(playerInJail); // next player on turn
        assertFalse(playerInJail.getOnFieldCards().contains(jail));

        // since the only card in the deck is a HEARTS card, the player should get out
        // of jail
        assertSame(player.getTable().getPlayerOnTurn(), playerInJail);
    }

    @Test
    public void testOnTurnStartNoHearts() {
        Player player = players.get(0); // on turn
        Player playerInJail = players.get(1);
        jail.use(player, playerInJail, null);
        player.getTable().setPlayerOnTurn(playerInJail); // next player on turn

        // change deck to SPADES card
        List<PlayCard> playCards = player.getTable().getDeck().getPlayCards();
        playCards.clear();
        Barrel barrel = new Barrel(Rank.ACE, Suit.SPADES);
        playCards.add(barrel);
        player.getTable().getDeck().setPlayCards(playCards);

        jail.onTurnStart(playerInJail);
        assertFalse(playerInJail.getOnFieldCards().contains(jail));

        // since the only card in the deck is a Spades card, it should be the next
        // players turn
        assertNotSame(playerInJail.getTable().getPlayerOnTurn(), playerInJail);
    }
}
