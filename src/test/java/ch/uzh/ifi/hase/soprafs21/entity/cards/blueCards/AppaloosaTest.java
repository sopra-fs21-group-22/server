package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.entity.Player;

public class AppaloosaTest {

    private Player player;
    private Appaloosa appa;

    @BeforeEach
    public void beforeEach() {
        player = new Player();
        appa = new Appaloosa();
    }

    @Test
    public void testDistanceDecreases() {
        appa.onPlacement(player, player);
        assertEquals(1, player.getDistanceDecreaseToOthers());
    }

    @Test
    public void testCardRemoval() {
        appa.onPlacement(player, player);
        appa.onRemoval(player);
        assertEquals(0, player.getDistanceDecreaseToOthers());
    }
}
