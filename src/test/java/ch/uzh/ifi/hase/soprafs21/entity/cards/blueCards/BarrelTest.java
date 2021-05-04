package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

public class BarrelTest {

    private Player player;
    private Barrel barrel = new Barrel(Rank.ACE, Suit.CLUBS);

    @BeforeEach
    public void beforeEach() {
        player = new Player();
        PlayerTable table = new PlayerTable();
        player.setTable(table);
        Deck discardPile = new Deck();
        discardPile.setPlayCards(new ArrayList<>());
        Deck deck = new Deck();
        List<PlayCard> cards = new ArrayList<>();
        cards.add(new Barrel(Rank.ACE, Suit.CLUBS));
        cards.add(new Barrel(Rank.EIGHT, Suit.HEARTS));

        deck.setPlayCards(cards);
        table.setDiscardPile(discardPile);
        table.setDeck(deck);
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
}
