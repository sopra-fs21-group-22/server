package ch.uzh.ifi.hase.soprafs21.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;

public class PlayerTableTest {

    private List<Player> players;
    private PlayerTable table;
    private List<GameRole> gameRoles = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        gameRoles.add(GameRole.SHERIFF);
        gameRoles.add(GameRole.OUTLAW);
        gameRoles.add(GameRole.OUTLAW);
        gameRoles.add(GameRole.RENEGADE);
        gameRoles.add(GameRole.DEPUTY);
        gameRoles.add(GameRole.OUTLAW);
        gameRoles.add(GameRole.DEPUTY);

        table = new PlayerTable();
        Deck deck = new Deck();
        Deck discardPile = new Deck();
        table.setDiscardPile(discardPile);
        table.setDeck(deck);
        for (int i = 0; i < 50; i++) {
            deck.addCard(new Bang(Rank.SEVEN, Suit.SPADES));
        }

        players = new ArrayList<>();
        Player oldPlayer = new Player();
        oldPlayer.setId(15L);
        players.add(oldPlayer);
        oldPlayer.setOnFieldCards(new OnFieldCards());
        oldPlayer.setGameRole(gameRoles.get(0));
        oldPlayer.setHand(new Hand());
        oldPlayer.setTable(table);

        for (int i = 0; i < 6; i++) {
            Player newPlayer = new Player();
            newPlayer.setGameRole(gameRoles.get(i + 1));
            newPlayer.setId(Long.valueOf(i));
            newPlayer.setOnFieldCards(new OnFieldCards());
            newPlayer.setHand(new Hand());
            newPlayer.setTable(table);
            players.add(newPlayer);
            newPlayer.setRightNeighbor(oldPlayer);
            oldPlayer.setLeftNeighbor(newPlayer);
            oldPlayer = newPlayer;
        }
        table.setPlayers(players);
        Player firstPlayer = players.get(0);
        Player lastPlayer = players.get(players.size() - 1);
        firstPlayer.setRightNeighbor(lastPlayer);
        lastPlayer.setLeftNeighbor(firstPlayer);
    }

    @Test
    public void testGetPlayersInRange() {
        Player player = players.get(0);
        List<Player> reachablePlayers = table.getPlayersInRangeOf(player.getId());
        assertEquals(2, reachablePlayers.size());
    }

    @Test
    public void testGetPlayersInRange_invalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            table.getPlayersInRangeOf(125412521L);
        });
    }

    @Test
    public void testGetPlayerById_invalidId() {
        Optional<Player> player = table.getPlayerById(123124L);
        assertEquals(Optional.empty(), player);
    }

    @Test
    public void testGetPlayerById() {
        Player player = players.get(0);
        Optional<Player> optPlayer = table.getPlayerById(player.getId());
        assertEquals(player, optPlayer.get());
    }
}
