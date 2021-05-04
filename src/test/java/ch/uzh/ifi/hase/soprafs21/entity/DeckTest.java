package ch.uzh.ifi.hase.soprafs21.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Barrel;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Schofield;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;

public class DeckTest {

    Deck deck;
    Deck discardPile;

    @BeforeEach
    public void beforeEach() {
        deck = new Deck();
        discardPile = new Deck();
        deck.setDiscardPile(discardPile);
        for (int i = 0; i < 2; i++) {
            deck.addCard(new Bang(Rank.SEVEN, Suit.HEARTS));
            deck.addCard(new Schofield(Rank.EIGHT, Suit.SPADES));
            deck.addCard(new Barrel(Rank.ACE, Suit.CLUBS));
        }
    }

    @Test
    public void testDrawMoreCardsThanAvailable() {
        List<PlayCard> cards = deck.drawCards(3);
        discardPile.setPlayCards(cards);
        // readds discardPile cards except the top card
        deck.drawCards(4);

        int expectedCardsInDeck = 1;
        assertEquals(expectedCardsInDeck, deck.getPlayCards().size());
    }

    @Test
    public void testDrawOrder() {
        assertEquals(Suit.CLUBS, deck.drawCards(1).get(0).getSuit());
    }

    @Test
    public void testCardOrderViolation() {
        // checking that the readded discard pile cards are beneath the cards that are
        // still in the deck
        List<PlayCard> cards = deck.drawCards(5);
        discardPile.setPlayCards(cards);
        List<PlayCard> drawnCards = deck.drawCards(3);
        assertEquals(Suit.HEARTS, drawnCards.get(0).getSuit());
    }
}
