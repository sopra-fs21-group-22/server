package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

public class VolcanicTest {

    private Player player;
    private List<Player> players;
    private Bang bang = new Bang(Rank.SEVEN, Suit.HEARTS);
    private Volcanic volcanic = new Volcanic(Rank.ACE, Suit.SPADES);
    private PlayerTable table = new PlayerTable();

    @BeforeEach
    public void beforeEach() {
        Deck deck = new Deck();
        table.setDeck(deck);

        // create a game with 7 players and their Hand & onField Cards
        players = new ArrayList<>();
        player = new Player();
        User user = new User();
        user.setUsername("Ada");
        player.setUser(user);
        player.setId(15L);
        player.setTable(table);
        table.setPlayerOnTurn(player); // players.get(0) onTurn
        table.setDiscardPile(new Deck());
        players.add(player);
        player.setOnFieldCards(new OnFieldCards());
        player.setHand(new Hand());

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
            newPlayer.setRightNeighbor(player);
            player.setLeftNeighbor(newPlayer);
            player = newPlayer;
        }
        Player firstPlayer = players.get(0);
        Player lastPlayer = players.get(players.size() - 1);
        firstPlayer.setRightNeighbor(lastPlayer);
        lastPlayer.setLeftNeighbor(firstPlayer);
    }

    @Test
    public void playingMultipleBang() {
        CharacterCard characterCard = new CharacterCard();
        characterCard.setLifeAmount(3);
        characterCard.setName("Paul Regret");
        Player target = player.getRightNeighbor();
        target.setCharacterCard(characterCard);
        int expectedBullets = target.getBullets() - 2;
        volcanic.use(player, player, null);
        bang.use(player, target, null);
        bang.use(player, target, null);

        assertEquals(expectedBullets, target.getBullets());
    }

    @Test
    public void undo() {
        CharacterCard characterCard = new CharacterCard();
        characterCard.setLifeAmount(3);
        characterCard.setName("Paul Regret");
        Player rightNeigbor = player.getRightNeighbor();
        rightNeigbor.setCharacterCard(characterCard);
        volcanic.use(player, player, null);
        volcanic.onRemoval(player);
        bang.use(player, rightNeigbor, null);

        assertThrows(GameLogicException.class, () -> {
            bang.use(player, rightNeigbor, null);
        });
    }
}
