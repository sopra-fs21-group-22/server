package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

public class SchofieldTest {

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
        Schofield card = new Schofield();
        Player player = players.get(0);
        card.use(player, players.subList(0, 1));
        assertEquals(player.getBaseRange()+1, player.getRange());
    }

    @Test
    public void testUndo() {
        Schofield card = new Schofield();
        Player player = players.get(0);
        card.use(player, players.subList(0, 1));
        assertEquals(player.getBaseRange()+1, player.getRange());
        card.onRemoval(player);
        assertEquals(player.getBaseRange(), player.getRange());
    }
}
