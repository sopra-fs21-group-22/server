package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

public class GatlingTest {

    private Player user;
    private List<Player> targets;
    private Gatling gatling;

    @BeforeEach
    public void beforeEach() {
        gatling = new Gatling();
        user = new Player();
        user.setId(15L);
        targets = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Player target = new Player();
            target.setId(Long.valueOf(i));
            targets.add(target);
        }
    }

    @Test
    public void cantHitCardOwner() {
        targets.add(user);
        assertThrows(GameLogicException.class, () -> {
            gatling.use(user, targets);
        });
    }

    @Test
    public void hitsEveryone() {
        List<Integer> expectedLives = new ArrayList<>();
        for (Player target : targets) {
            expectedLives.add(target.getBullets() - 1);
        }
        gatling.use(user, targets);

        List<Integer> actualLives = new ArrayList<>();
        for (Player target : targets) {
            actualLives.add(target.getBullets());
        }

        for (Integer lives : actualLives) {
            expectedLives.remove(lives);
        }
        assertTrue(expectedLives.size() == 0);
    }
}
