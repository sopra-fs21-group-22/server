package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

public class WinchesterTest {

    private List<Player> players;

    @BeforeEach
    public void beforeEach() {
        players = new ArrayList<>();
        Player oldPlayer = new Player();
        oldPlayer.setId(15L);
        players.add(oldPlayer);
        oldPlayer.setOnFieldCards(new OnFieldCards());

        for (int i = 0; i < 6; i++) {
            Player newPlayer = new Player();
            newPlayer.setId(Long.valueOf(i));
            newPlayer.setOnFieldCards(new OnFieldCards());
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
    public void testRange() {
        Winchester card = new Winchester();
        Player player = players.get(0);
        card.use(player, players.subList(0, 1));
        assertEquals(player.getBaseRange()+4, player.getRange());
    }

    @Test
    public void testUndo() {
        Winchester card = new Winchester();
        Player player = players.get(0);
        card.use(player, players.subList(0, 1));
        assertEquals(player.getBaseRange()+4, player.getRange());
        card.onRemoval(player);
        assertEquals(player.getBaseRange(), player.getRange());
    }
}
