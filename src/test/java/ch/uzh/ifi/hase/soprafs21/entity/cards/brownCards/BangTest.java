package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

public class BangTest {

    private List<Player> players;
    private List<Player> targets;
    private Bang bang = new Bang(Rank.SEVEN, Suit.HEARTS);
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
    public void testBang_reducesLives() {
        CharacterCard characterCard = new CharacterCard();
        characterCard.setLifeAmount(3);
        characterCard.setName("Paul Regret");
        Player user = players.get(0);
        Player target = user.getRightNeighbor();
        target.setCharacterCard(characterCard);
        int expectedLives = target.getBullets() - 1;

        bang.use(user, target, null);

        assertEquals(expectedLives, target.getBullets());
    }

    @Test
    public void cantUseBangOnYourself() {
        Player user = players.get(0);
        assertThrows(GameLogicException.class, () -> {
            bang.use(user, user, null);
        });
    }

    @Test
    public void cantPlayMoreBangCards() {
        CharacterCard characterCard = new CharacterCard();
        characterCard.setLifeAmount(3);
        characterCard.setName("Paul Regret");
        Player user = players.get(0);
        Player target = user.getRightNeighbor();
        target.setCharacterCard(characterCard);
        bang.use(user, target, null);
        assertThrows(GameLogicException.class, () -> {
            bang.use(user, target, null);
        });
    }

    @Test
    public void outOfRange_CantAttack() {
        Player user = players.get(0);
        Player target = user.getRightNeighbor().getRightNeighbor();
        assertThrows(GameLogicException.class, () -> {
            bang.use(user, target, null);
        });
    }
}
