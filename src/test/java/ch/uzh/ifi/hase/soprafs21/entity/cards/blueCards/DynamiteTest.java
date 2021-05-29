package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;
import jdk.jfr.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DynamiteTest {
    private List<Player> players;

    private BlueCard dynamite = new Dynamite(Rank.SEVEN, Suit.SPADES);

    private PlayerTable table = new PlayerTable();

    @BeforeEach
    public void beforeEach() {
        dynamite.setId(11L);

        // create a table with a deck and a discard pile
        Deck deck = new Deck();
        deck.addCard(new Bang(Rank.ACE, Suit.SPADES));
        deck.addCard(new Bang(Rank.ACE, Suit.SPADES));
        deck.addCard(new Bang(Rank.ACE, Suit.SPADES));
        deck.addCard(new Bang(Rank.ACE, Suit.SPADES));

        table.setDeck(deck);
        table.setDiscardPile(new Deck());

        // create a game with 7 players and their Hand & onField Cards.
        players = new ArrayList<>();
        table.setPlayers(players);
        Player oldPlayer = new Player();
        User user = new User();
        user.setUsername("Ada");
        oldPlayer.setUser(user);
        oldPlayer.setId(15L);
        oldPlayer.setTable(table);
        table.setDiscardPile(new Deck());
        table.setPlayerOnTurn(oldPlayer); // players.get(0) onTurn
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
    public void testOnPlacement() {
        Player playerWithDynamite = players.get(0);

        dynamite.use(playerWithDynamite, playerWithDynamite, null);
        assertTrue(playerWithDynamite.getOnFieldCards().contains(dynamite));
    }

    @Test
    public void testOnTurnStartNoExplosion() {
        Player playerWithDynamite = players.get(0); // on turn
        playerWithDynamite.setBullets(4);

        dynamite.use(playerWithDynamite, playerWithDynamite, null);

        ArrayList<PlayCard> playCards = new ArrayList<>();
        Bang bang = new Bang(Rank.THREE, Suit.HEARTS);
        playCards.add(bang);
        table.getDeck().setPlayCards(playCards);

        dynamite.onTurnStart(playerWithDynamite);
        // there shouldn't be an explosion, so the dynamite card gets moved to the left
        assertFalse(playerWithDynamite.getOnFieldCards().contains(dynamite));
        assertTrue(playerWithDynamite.getLeftNeighbor().getOnFieldCards().contains(dynamite));
        assertEquals(4, playerWithDynamite.getBullets());
    }

    @Test
    public void testOnTurnStartExplosionPlayerLives() {
        CharacterCard characterCard = new CharacterCard();
        characterCard.setLifeAmount(3);
        characterCard.setName("Paul Regret");
        Player playerWithDynamite = players.get(0); // on turn
        playerWithDynamite.setBullets(4);

        dynamite.use(playerWithDynamite, playerWithDynamite, null);

        ArrayList<PlayCard> playCards = new ArrayList<>();
        Bang bang = new Bang(Rank.THREE, Suit.SPADES);
        playCards.add(bang);
        table.getDeck().setPlayCards(playCards);
        playerWithDynamite.setCharacterCard(characterCard);

        dynamite.onTurnStart(playerWithDynamite); // DYNAMITE PLAYED

        // there is an explosion, so the dynamite card gets NOT moved to the left
        assertEquals(1, playerWithDynamite.getBullets());
        assertFalse(playerWithDynamite.getOnFieldCards().contains(dynamite));
        assertFalse(playerWithDynamite.getLeftNeighbor().getOnFieldCards().contains(dynamite));
    }

    @Test
    public void testOnTurnStartExplosionPlayerDies() {
        CharacterCard characterCard = new CharacterCard();
        characterCard.setLifeAmount(3);
        characterCard.setName("Paul Regret");
        Player playerWithDynamite = players.get(0); // on turn
        playerWithDynamite.setCharacterCard(characterCard);
        playerWithDynamite.setBullets(3);

        dynamite.use(playerWithDynamite, playerWithDynamite, null);

        ArrayList<PlayCard> playCards = new ArrayList<>();
        Bang bang = new Bang(Rank.THREE, Suit.SPADES);
        playCards.add(bang);
        table.getDeck().setPlayCards(playCards);

        dynamite.onTurnStart(playerWithDynamite); // DYNAMITE PLAYED

        // there is an explosion, so the dynamite card gets NOT moved to the left
        // TODO once death is handled test death here
        assertEquals(0, playerWithDynamite.getBullets());
        assertFalse(playerWithDynamite.getOnFieldCards().contains(dynamite));
        assertFalse(playerWithDynamite.getLeftNeighbor().getOnFieldCards().contains(dynamite));
    }

    @Test
    public void testDynamiteMovesCorrectly() {
        Player player = players.get(0);
        dynamite.use(player, player, null);
        dynamite.onTurnStart(player);
        assertTrue(player.getLeftNeighbor().getOnFieldCards().getOnFieldCards().contains(dynamite));
    }

    @Test
    public void testDynamiteMove_skipsCorpses() {
        Player player = players.get(0);
        player.getLeftNeighbor().setBullets(0);
        dynamite.use(player, player, null);
        dynamite.onTurnStart(player);
        assertTrue(player.getLeftNeighbor().getLeftNeighbor().getOnFieldCards().getOnFieldCards().contains(dynamite));
    }

    @Test
    public void dynamiteDeath_removesAllCards() {
        Player player = players.get(0);
        player.setBullets(3);
        player.getHand().addCard(new Bang(Rank.TWO, Suit.SPADES));
        player.getOnFieldCards().addOnFieldCard(new Schofield(Rank.SIX, Suit.DIAMONDS));

        ArrayList<PlayCard> playCards = new ArrayList<>();
        Bang bang = new Bang(Rank.THREE, Suit.SPADES);
        playCards.add(bang);
        table.getDeck().setPlayCards(playCards);

        dynamite.onTurnStart(player);
        assertEquals(0, player.getBullets());
        assertEquals(0, player.getOnFieldCards().getOnFieldCards().size());
        assertEquals(0, player.getHand().getPlayCards().size());
    }

    @Test
    public void testDynamiteAndJail() {
        Player player = players.get(0);
        player.setBullets(3);
        player.getHand().addCard(new Bang(Rank.TWO, Suit.SPADES));
        OnFieldCards onFieldCards = player.getOnFieldCards();
        onFieldCards.addOnFieldCard(new Dynamite(Rank.SIX, Suit.DIAMONDS));
        onFieldCards.addOnFieldCard(new Jail(Rank.SIX, Suit.DIAMONDS));

        int numCards = onFieldCards.getLength();
        for (int i = 0; i < onFieldCards.getLength(); i++) {
            BlueCard currCard = onFieldCards.get(i);
            currCard.onTurnStart(player);
            if (onFieldCards.getLength() != numCards) {
                i -= (numCards - onFieldCards.getLength());
                numCards = onFieldCards.getLength();
            }
        }
    }

}
