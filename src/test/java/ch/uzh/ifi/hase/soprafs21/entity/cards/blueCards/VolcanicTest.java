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
    private List<Player> targets;
    private Bang bang;

    @BeforeEach
    public void beforeEach() {
        player = new Player();
        player.setId(1L);
        Player target = new Player();
        target.setId(2L);
        bang = new Bang();
        targets = new ArrayList<>();
        targets.add(target);
    }

    @Test
    public void playingMultipleBang() {
        Volcanic card = new Volcanic();

        int expectedBullets = targets.get(0).getBullets() - 2;

        card.use(player, new ArrayList<>());
        bang.use(player, targets);
        bang.use(player, targets);

        assertEquals(expectedBullets, targets.get(0).getBullets());
    }

    @Test
    public void undo() {
        Volcanic card = new Volcanic();

        card.use(player, new ArrayList<>());
        card.undo(player);
        bang.use(player, targets);

        assertThrows(GameLogicException.class, () -> {
            bang.use(player, targets);
        });
    }
}
