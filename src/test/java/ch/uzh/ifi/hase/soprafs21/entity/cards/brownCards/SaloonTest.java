package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

public class SaloonTest {

    private Saloon saloon;
    private Player user;

    @BeforeEach
    public void beforeEach() {
        saloon = new Saloon();
        user = new Player();
        user.setId(1L);
    }

    @Test
    public void testSaloon_addsBullets() {
        user.setBullets(user.getBullets() - 1);

        int expectedBullets = user.getBullets() + 1;

        saloon.use(user, new ArrayList<Player>());

        int actualBullets = user.getBullets();

        assertEquals(expectedBullets, actualBullets);
    }

    @Test
    public void saloonAlreadyMaxHealth() {
        assertThrows(GameLogicException.class, () -> {
            saloon.use(user, new ArrayList<Player>());
        });
    }

}
