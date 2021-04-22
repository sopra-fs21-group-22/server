package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

public class BeerTest {
    private Beer beer;
    private Player user;

    @BeforeEach
    public void beforeEach() {
        beer = new Beer();
        user = new Player();
        user.setId(1L);
    }

    @Test
    public void testBeer_addsBullets() {
        user.setBullets(user.getBullets() - 1);

        int expectedBullets = user.getBullets() + 1;

        beer.use(user, new ArrayList<Player>());

        int actualBullets = user.getBullets();

        assertEquals(expectedBullets, actualBullets);
    }

    @Test
    public void BeerAlreadyMaxHealth() {
        assertThrows(GameLogicException.class, () -> {
            beer.use(user, new ArrayList<Player>());
        });
    }
}
