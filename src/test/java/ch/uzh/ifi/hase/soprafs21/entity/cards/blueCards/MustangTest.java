package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

public class MustangTest {

    private List<Player> players;

    private BlueCard mustang = new Mustang(Rank.ACE, Suit.SPADES);

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
    public void testDistanceIncrease() {
        Player player = players.get(0);
        mustang.use(player, player, null);
        assertEquals(1, player.getDistanceIncreaseForOthers());
    }

    @Test
    public void testDistanceIncrease_undo() {
        Player player = players.get(0);
        mustang.use(player, player, null);
        mustang.onRemoval(player);
        assertEquals(0, player.getDistanceIncreaseForOthers());
    }

    @Test
    public void testOnlyOneHorseATime() {
        Appaloosa appa = new Appaloosa(Rank.ACE, Suit.SPADES);
        Player player = players.get(0);
        mustang.use(player, player, null);
        appa.use(player, player, null);
        assertEquals(1, player.getOnFieldCards().getLength());
    }

}
