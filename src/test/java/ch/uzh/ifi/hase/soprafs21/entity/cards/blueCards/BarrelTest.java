package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

public class BarrelTest {

    private Player player;
    private List<Player> players;
    private Barrel barrel = new Barrel(Rank.ACE, Suit.SPADES);
    private PlayerTable table = new PlayerTable();

    @BeforeEach
    public void beforeEach() {
        Deck deck = new Deck();
        List<PlayCard> cards = new ArrayList<>();
        cards.add(new Barrel(Rank.ACE, Suit.CLUBS));
        cards.add(new Barrel(Rank.EIGHT, Suit.HEARTS));

        deck.setPlayCards(cards);
        table.setDeck(deck);

        // create a game with 7 players and their Hand & onField Cards
        players = new ArrayList<>();
        player = new Player();
        User user = new User();
        user.setUsername("Ada");
        player.setUser(user);
        player.setId(15L);
        player.setTable(table);
        table.setPlayerOnTurn(player); // players.get(0) onTurn
        table.setDiscardPile(new Deck());
        players.add(player);
        player.setOnFieldCards(new OnFieldCards());
        player.setHand(new Hand());

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
            newPlayer.setRightNeighbor(player);
            player.setLeftNeighbor(newPlayer);
            player = newPlayer;
        }
        Player firstPlayer = players.get(0);
        Player lastPlayer = players.get(players.size() - 1);
        firstPlayer.setRightNeighbor(lastPlayer);
        lastPlayer.setLeftNeighbor(firstPlayer);
    }

    @Test
    public void testOnBang_noHeart() {
        assertFalse(barrel.onBang(player));
    }

    @Test
    public void testOnBang_Heart() {
        player.getTable().getDeck().drawCards(1);
        assertTrue(barrel.onBang(player));
    }

    @Test
    public void testOnBang_CardAddedToDiscardPile() {
        int numCardsDiscPile = player.getTable().getDiscardPile().getPlayCards().size();
        barrel.onBang(player);
        assertEquals(numCardsDiscPile + 1, player.getTable().getDiscardPile().getPlayCards().size());
    }

    @Test
    public void testPlayingTwoBarrels_throws() {
        barrel.use(player, player, null);
        assertThrows(GameLogicException.class, () -> {
            barrel.use(player, player, null);
        });
    }
}
