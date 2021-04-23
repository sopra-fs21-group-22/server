package ch.uzh.ifi.hase.soprafs21.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private List<Player> players;

    @BeforeEach
    public void beforeEach() {
        players = new ArrayList<>();
        Player oldPlayer = new Player();
        oldPlayer.setId(15L);
        players.add(oldPlayer);
        oldPlayer.setOnFieldCards(new OnFieldCards());

        for (int i = 0; i < 7; i++) {
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
    public void testWeaponRange() {
        Player player = players.get(0);
        assertTrue(player.reachesWithWeapon(player.getRightNeighbor()));
        assertTrue(player.reachesWithWeapon(player.getLeftNeighbor()));
    }

    @Test
    public void testWeaponRange_changedRange_inRange() {
        Player player = players.get(0);
        player.setRange(3);
        assertTrue(player.reachesWithWeapon(player.getLeftNeighbor().getLeftNeighbor().getLeftNeighbor()));
    }

    @Test
    public void testWeaponRange_changedRange_outOfRange() {
        Player player = players.get(0);
        player.setRange(3);
        assertFalse(player
                .reachesWithWeapon(player.getLeftNeighbor().getLeftNeighbor().getLeftNeighbor().getLeftNeighbor()));
    }

}
