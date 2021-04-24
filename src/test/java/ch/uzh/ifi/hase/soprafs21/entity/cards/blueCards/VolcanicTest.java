package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

public class VolcanicTest {

    private Player player;
    private List<Player> bangTargets;
    private List<Player> volcanicTargets;
    private Bang bang;

    @BeforeEach
    public void beforeEach() {
        player = new Player();
        player.setId(1L);
        Player target = new Player();
        target.setId(2L);
        player.setRightNeighbor(target);
        player.setLeftNeighbor(target);
        target.setLeftNeighbor(player);
        target.setRightNeighbor(player);

        bang = new Bang();
        bangTargets = new ArrayList<>();
        bangTargets.add(target);
    }

    @Test
    public void playingMultipleBang() {
        Volcanic card = new Volcanic();

        int expectedBullets = bangTargets.get(0).getBullets() - 2;

        card.use(player, new ArrayList<>());
        bang.use(player, bangTargets);
        bang.use(player, bangTargets);

        assertEquals(expectedBullets, bangTargets.get(0).getBullets());
    }

    @Test
    public void undo() {
        Volcanic card = new Volcanic();

        card.use(player, new ArrayList<>());
        card.onRemoval(player);
        bang.use(player, bangTargets);

        assertThrows(GameLogicException.class, () -> {
            bang.use(player, bangTargets);
        });
    }
}
