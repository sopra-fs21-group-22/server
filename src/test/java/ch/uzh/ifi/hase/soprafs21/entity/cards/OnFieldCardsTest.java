package ch.uzh.ifi.hase.soprafs21.entity.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Schofield;

public class OnFieldCardsTest {

    private Schofield schofield = new Schofield(Rank.ACE, Suit.HEARTS);
    PlayerTable table = new PlayerTable();
    List<Player> players;

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
        OnFieldCards onfieldCards = new OnFieldCards();
        onfieldCards.setPlayer(oldPlayer);
        oldPlayer.setOnFieldCards(onfieldCards);

        oldPlayer.setHand(new Hand());

        for (int i = 0; i < 6; i++) {
            Player newPlayer = new Player();
            user = new User();
            user.setUsername("Ada");
            newPlayer.setUser(user);
            newPlayer.setId(Long.valueOf(i));
            onfieldCards = new OnFieldCards();
            newPlayer.setOnFieldCards(onfieldCards);
            onfieldCards.setPlayer(newPlayer);
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
    public void removeOnFieldCard_revertsEffects() {
        Player player = players.get(0);
        OnFieldCards onFieldCards = player.getOnFieldCards();
        int intitialRange = player.getRange();
        int expectedRange = intitialRange + 1;

        schofield.use(player, player, null);
        assertEquals(expectedRange, player.getRange());

        onFieldCards.removeOnFieldCard(schofield);
        assertEquals(intitialRange, player.getRange());
    }
}
