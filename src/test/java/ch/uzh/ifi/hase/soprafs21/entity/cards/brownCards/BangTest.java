package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

public class BangTest {

    private Player user;
    private Bang bang;
    private Player target;
    private List<Player> targets;

    @BeforeEach
    public void beforeEach() {
        bang = new Bang();
        user = new Player();
        user.setId(2L);
        targets = new ArrayList<>();
        target = new Player();
        target.setId(5L);
        targets.add(target);
    }

    @Test
    public void testBang_reducesLives() {

        int expectedLives = target.getBullets() - 1;

        bang.use(user, targets);

        assertEquals(expectedLives, targets.get(0).getBullets());
    }

    @Test
    public void cantUseBangOnYourself() {
        targets.remove(target);
        targets.add(user);
        assertThrows(GameLogicException.class, () -> {
            bang.use(user, targets);
        });
    }

    @Test
    public void cantPlayMoreBangCards() {
        bang.use(user, targets);
        assertThrows(GameLogicException.class, () -> {
            bang.use(user, targets);
        });
    }
}
