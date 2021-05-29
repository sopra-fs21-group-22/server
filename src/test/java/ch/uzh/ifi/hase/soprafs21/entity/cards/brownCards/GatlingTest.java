package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class GatlingTest {

    private List<Player> players;
    private Gatling gatling = new Gatling(Rank.SEVEN, Suit.HEARTS);
    private PlayerTable table = new PlayerTable();

    @BeforeEach
    public void beforeEach() {
        Deck deck = new Deck();
        table.setDeck(deck);

        // create a game with 7 players and their Hand & onField Cards
        players = new ArrayList<>();
        Player oldPlayer = new Player();
        User user = new User();
        user.setUsername("Ada");
        oldPlayer.setUser(user);
        oldPlayer.setId(15L);
        oldPlayer.setTable(table);
        table.setPlayerOnTurn(oldPlayer); // players.get(0) onTurn
        table.setDiscardPile(new Deck());
        table.setPlayers(players);
        players.add(oldPlayer);
        OnFieldCards onfieldCards = new OnFieldCards();
        oldPlayer.setOnFieldCards(onfieldCards);
        onfieldCards.setPlayer(oldPlayer);
        oldPlayer.setHand(new Hand());

        for (int i = 0; i < 6; i++) {
            Player newPlayer = new Player();
            user = new User();
            user.setUsername("Ada");
            newPlayer.setUser(user);
            newPlayer.setId(Long.valueOf(i));
            onfieldCards = new OnFieldCards();
            onfieldCards.setPlayer(newPlayer);
            newPlayer.setOnFieldCards(onfieldCards);
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
    public void cantHitCardOwner() {
        Player player = players.get(0);
        int expectedBullets = player.getBullets();
        gatling.use(player, player, null);

        assertEquals(expectedBullets, player.getBullets());
    }

    @Test
    public void hitsEveryoneExceptUser() {
        Player player = players.get(0);

        List<Integer> expectedBulletsEnemies = new ArrayList<>();
        for (int i = 1; i < players.size(); i++) {
            expectedBulletsEnemies.add(players.get(i).getBullets() - 1);
        }

        gatling.use(player, player, null);

        for (int i = 1; i < players.size(); i++) {
            assertEquals(players.get(i).getBullets(), expectedBulletsEnemies.get(i - 1));
        }
    }
}
