package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;

public class SaloonTest {

    private Saloon saloon = new Saloon(Rank.THREE, Suit.CLUBS);
    private List<Player> players;
    private PlayerTable table = new PlayerTable();

    @BeforeEach
    public void beforeEach() {
        Deck deck = new Deck();
        table.setDeck(deck);

        // create a game with 7 players and their Hand & onField Cards
        players = new ArrayList<>();
        table.setPlayers(players);
        Player oldPlayer = new Player();
        User user = new User();
        user.setUsername("Ada");
        oldPlayer.setUser(user);
        oldPlayer.setId(15L);
        oldPlayer.setTable(table);
        table.setPlayerOnTurn(oldPlayer); // players.get(0) onTurn
        table.setDiscardPile(new Deck());
        players.add(oldPlayer);
        oldPlayer.setOnFieldCards(new OnFieldCards());
        oldPlayer.setHand(new Hand());

        for (int i = 0; i < 6; i++) {
            Player newPlayer = new Player();
            user = new User();
            user.setUsername("Ada");
            newPlayer.setUser(user);
            newPlayer.setId(Long.valueOf(i));
            newPlayer.setOnFieldCards(new OnFieldCards());
            newPlayer.setHand(new Hand());
            newPlayer.setTable(table);
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
    public void testSaloon_addsBullets() {
        Player user = players.get(0);
        user.setBullets(user.getBullets() - 1);

        int expectedBullets = user.getBullets() + 1;

        saloon.use(user, user, null);

        int actualBullets = user.getBullets();

        assertEquals(expectedBullets, actualBullets);
    }

    @Test
    public void testSaloonOverheals(){
        Player player = players.get(0);
        player.setBullets(player.getMaxBullets());
        saloon.use(players.get(1), players.get(1), null);
        assertEquals(player.getBullets(), player.getMaxBullets());
    }
}
