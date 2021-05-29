package ch.uzh.ifi.hase.soprafs21.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Appaloosa;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Carabine;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Dynamite;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class HandTest {

    private List<Player> players;

    @BeforeEach
    public void beforeEach() {
        PlayerTable table = new PlayerTable();
        Deck deck = new Deck();
        Deck discardPile = new Deck();
        table.setDiscardPile(discardPile);
        table.setDeck(deck);
        for (int i = 0; i < 50; i++) {
            deck.addCard(new Bang(Rank.SEVEN, Suit.SPADES));
            deck.addCard(new Dynamite(Rank.ACE, Suit.SPADES));
        }

        players = new ArrayList<>();
        Player oldPlayer = new Player();
        oldPlayer.setId(15L);
        players.add(oldPlayer);
        oldPlayer.setOnFieldCards(new OnFieldCards());
        oldPlayer.setHand(new Hand());
        oldPlayer.getHand().setPlayer(oldPlayer);
        oldPlayer.setTable(table);

        for (int i = 0; i < 7; i++) {
            Player newPlayer = new Player();
            newPlayer.setId(Long.valueOf(i));
            newPlayer.setOnFieldCards(new OnFieldCards());
            newPlayer.setHand(new Hand());
            newPlayer.getHand().setPlayer(newPlayer);
            newPlayer.setTable(table);
            players.add(newPlayer);
            newPlayer.setRightNeighbor(oldPlayer);
            oldPlayer.setLeftNeighbor(newPlayer);
            oldPlayer = newPlayer;
        }
        table.setPlayers(players);
        Player firstPlayer = players.get(0);
        Player lastPlayer = players.get(players.size() - 1);
        firstPlayer.setRightNeighbor(lastPlayer);
        lastPlayer.setLeftNeighbor(firstPlayer);
    }

    @Test
    public void test_addCardInOrder() {
        Player player = players.get(0);
        PlayCard bang = new Bang(Rank.SEVEN, Suit.SPADES);
        player.getHand().addCardInOrder(bang);
        assertEquals(player.getHand().get(0).getCard(), bang.getCard());

        PlayCard appaloosa = new Appaloosa(Rank.ACE, Suit.SPADES);
        player.getHand().addCardInOrder(appaloosa);

        assertEquals(player.getHand().get(1).getCard(), appaloosa.getCard());
        assertEquals(player.getHand().get(0).getCard(), bang.getCard());
    }

    @Test
    public void test_addCard() {
        Player player = players.get(0);
        PlayCard bang = new Bang(Rank.SEVEN, Suit.SPADES);
        player.getHand().addCard(bang);
        assertEquals(player.getHand().get(0).getCard(), bang.getCard());

        PlayCard appaloosa = new Appaloosa(Rank.ACE, Suit.SPADES);
        player.getHand().addCard(appaloosa);

        assertEquals(player.getHand().get(0).getCard(), bang.getCard());
        assertEquals(player.getHand().get(1).getCard(), appaloosa.getCard());
    }

    @Test
    public void test_addCard_dynamiteDirectlyPlayed() {
        Player player = players.get(0);
        PlayCard bang = new Bang(Rank.SEVEN, Suit.SPADES);
        player.getHand().addCard(bang);
        assertEquals(player.getHand().get(0).getCard(), bang.getCard());

        PlayCard dynamite = new Dynamite(Rank.ACE, Suit.SPADES);
        player.getHand().addCard(dynamite);
        assertEquals(player.getHand().get(0).getCard(), bang.getCard());
        assertEquals(Card.DYNAMITE, player.getOnFieldCards().getOnFieldCards().get(0).getCard());
    }

    @Test
    public void test_addCards() {
        Player player = players.get(0);
        List<PlayCard> cards = new ArrayList<>();
        cards.add(new Bang(Rank.TEN, Suit.SPADES));
        cards.add(new Carabine(Rank.SEVEN, Suit.CLUBS));
        cards.add(new Appaloosa(Rank.ACE, Suit.HEARTS));
        cards.add(new Bang(Rank.KING, Suit.SPADES));
        player.getHand().addCards(cards);

        assertEquals(Card.BANG, player.getHand().get(0).getCard());
        assertEquals(Card.BANG, player.getHand().get(1).getCard());
        assertEquals(Card.APPALOOSA, player.getHand().get(2).getCard());
        assertEquals(Card.CARABINE, player.getHand().get(3).getCard());
    }

    @Test
    public void removeCardById() {
        Player player = players.get(0);
        List<PlayCard> cards = new ArrayList<>();
        PlayCard bang = new Bang(Rank.SEVEN, Suit.HEARTS);
        bang.setId(12L);
        cards.add(bang);
        PlayCard carab = new Carabine(Rank.SEVEN, Suit.CLUBS);
        carab.setId(13L);
        cards.add(carab);
        PlayCard dyna = new Dynamite(Rank.SIX, Suit.SPADES);
        dyna.setId(14L);
        cards.add(dyna);

        player.getHand().addCards(cards);

        PlayCard card = player.getHand().getCardById(12L);
        assertEquals(bang, card);
    }

    @Test
    public void removeRandomCard_noHandCards_throws() {
        Player player = players.get(0);
        assertThrows(GameLogicException.class, () -> player.getHand().removeRandomCard());
    }

}
