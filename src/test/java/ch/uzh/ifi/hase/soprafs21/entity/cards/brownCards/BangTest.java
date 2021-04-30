package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

public class BangTest {

    private List<Player> players;
    private List<Player> targets;
    private Bang bang = new Bang();

    @BeforeEach
    public void beforeEach() {
        PlayerTable table = new PlayerTable();
        Deck deck = new Deck();
        deck.setPlayCards(new ArrayList<>());
        table.setDiscardPile(deck);
        targets = new ArrayList<>();
        players = new ArrayList<>();
        Player oldPlayer = new Player();
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
    public void testBang_reducesLives() {

        CharacterCard characterCard = new CharacterCard("Paul Regret", 3);
        Player user = players.get(0);
        Player target = user.getRightNeighbor();
        targets.add(target);
        target.setCharacterCard(characterCard);
        int expectedLives = target.getBullets() - 1;

        bang.use(user, targets);

        assertEquals(expectedLives, target.getBullets());
    }

    @Test
    public void cantUseBangOnYourself() {
        Player user = players.get(0);
        Player target = user.getRightNeighbor();
        targets.add(user);
        assertThrows(GameLogicException.class, () -> {
            bang.use(user, targets);
        });
    }

    @Test
    public void cantPlayMoreBangCards() {
        CharacterCard characterCard = new CharacterCard("Paul Regret", 3);
        Player user = players.get(0);
        Player target = user.getRightNeighbor();
        targets.add(target);
        target.setCharacterCard(characterCard);
        bang.use(user, targets);
        assertThrows(GameLogicException.class, () -> {
            bang.use(user, targets);
        });
    }

    @Test
    public void outOfRange_CantAttack() {
        Player user = players.get(0);
        Player target = user.getRightNeighbor().getRightNeighbor();
        targets.add(target);
        assertThrows(GameLogicException.class, () -> {
            bang.use(user, targets);
        });
    }
}
