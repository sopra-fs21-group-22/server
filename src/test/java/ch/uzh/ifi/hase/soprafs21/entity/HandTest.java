package ch.uzh.ifi.hase.soprafs21.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Carabine;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Dynamite;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;
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
        oldPlayer.setTable(table);

        for (int i = 0; i < 7; i++) {
            Player newPlayer = new Player();
            newPlayer.setId(Long.valueOf(i));
            newPlayer.setOnFieldCards(new OnFieldCards());
            newPlayer.setHand(new Hand());
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

        PlayCard dynamite = new Dynamite(Rank.ACE, Suit.SPADES);
        player.getHand().addCardInOrder(dynamite);

        assertEquals(player.getHand().get(0).getCard(), dynamite.getCard());
        assertEquals(player.getHand().get(1).getCard(), bang.getCard());
    }

    @Test
    public void test_addCard() {
        Player player = players.get(0);
        PlayCard bang = new Bang(Rank.SEVEN, Suit.SPADES);
        player.getHand().addCard(bang);
        assertEquals(player.getHand().get(0).getCard(), bang.getCard());

        PlayCard dynamite = new Dynamite(Rank.ACE, Suit.SPADES);
        player.getHand().addCard(dynamite);

        assertEquals(player.getHand().get(0).getCard(), dynamite.getCard());
        assertEquals(player.getHand().get(1).getCard(), bang.getCard());
    }

    @Test
    public void test_addCards() {
        Player player = players.get(0);
        List<PlayCard> cards = new ArrayList<>();
        cards.add(new Bang(Rank.TEN, Suit.SPADES));
        cards.add(new Carabine(Rank.SEVEN, Suit.CLUBS));
        cards.add(new Dynamite(Rank.ACE, Suit.HEARTS));
        cards.add(new Bang(Rank.KING, Suit.SPADES));
        player.getHand().addCards(cards);

        assertEquals(player.getHand().get(0).getCard(), Card.DYNAMITE);
        assertEquals(player.getHand().get(1).getCard(), Card.BANG);
        assertEquals(player.getHand().get(2).getCard(), Card.BANG);
        assertEquals(player.getHand().get(3).getCard(), Card.CARABINE);
    }

}
