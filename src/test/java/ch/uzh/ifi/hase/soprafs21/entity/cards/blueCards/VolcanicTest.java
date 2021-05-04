package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

public class VolcanicTest {

    private Player player;
    private List<Player> bangTargets;
    private List<Player> players;
    private List<Player> volcanicTargets;
    private Bang bang;

    @BeforeEach
    public void beforeEach() {
        bang = new Bang();
        PlayerTable table = new PlayerTable();
        Deck deck = new Deck();
        deck.setPlayCards(new ArrayList<>());
        table.setDiscardPile(deck);
        bangTargets = new ArrayList<>();
        players = new ArrayList<>();
        Player oldPlayer = new Player();
        player = oldPlayer;
        oldPlayer.setId(15L);
        players.add(oldPlayer);
        oldPlayer.setTable(table);
        oldPlayer.setOnFieldCards(new OnFieldCards());
        Hand hand = new Hand();
        hand.setPlayCards(new ArrayList<>());
        oldPlayer.setHand(hand);

        for (int i = 0; i < 6; i++) {
            Player newPlayer = new Player();
            newPlayer.setId(Long.valueOf(i));
            newPlayer.setOnFieldCards(new OnFieldCards());
            newPlayer.setHand(hand);
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
    public void playingMultipleBang() {
        Volcanic card = new Volcanic();
        bangTargets.add(players.get(1));
        int expectedBullets = bangTargets.get(0).getBullets() - 2;

        card.use(player, new ArrayList<>());
        bang.use(player, bangTargets);
        bang.use(player, bangTargets);

        assertEquals(expectedBullets, bangTargets.get(0).getBullets());
    }

    @Test
    public void undo() {
        Volcanic card = new Volcanic();

        bangTargets.add(players.get(1));

        card.use(player, new ArrayList<>());
        card.onRemoval(player);
        bang.use(player, bangTargets);

        assertThrows(GameLogicException.class, () -> {
            bang.use(player, bangTargets);
        });
    }
}
