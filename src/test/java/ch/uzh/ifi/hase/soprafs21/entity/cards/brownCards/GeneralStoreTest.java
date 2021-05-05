package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GeneralStoreTest {
    private GeneralStore generalStore;
    private List<Player> players;
    private PlayerTable table = new PlayerTable();

    @BeforeEach
    public void beforeEach() {
        table.setId(33L);
        generalStore = new GeneralStore(Rank.ACE, Suit.HEARTS);
        generalStore.setId(12L);
        Deck deck = new Deck();
        deck.setId(11L);
        VisibleCards visibleCards = new VisibleCards();
        table.setVisibleCards(visibleCards);
        ArrayList<PlayCard> playCards = new ArrayList<>();

        // create a game with 7 players and their Hand & onField Cards
        players = new ArrayList<>();
        Player oldPlayer = new Player();
        oldPlayer.setId(15L);
        players.add(oldPlayer);
        oldPlayer.setTable(table);

        for (int i = 0; i < 6; i++) {
            Bang bang = new Bang(Rank.ACE, Suit.HEARTS);
            playCards.add(bang);
            Player newPlayer = new Player();
            newPlayer.setTable(table);
            newPlayer.setId(Long.valueOf(i));
            players.add(newPlayer);
        }
        Bang bang = new Bang(Rank.ACE, Suit.HEARTS);
        playCards.add(bang);
        deck.setPlayCards(playCards);
        table.setDeck(deck);
    }

    // @Test
    // public void testOnPlacement(){
    // generalStore.onPlacement(players.get(0), players);
    // assertEquals(7, table.getVisibleCards().getLength());
    // }

}
