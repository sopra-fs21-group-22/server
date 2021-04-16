package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.BrownCard;

@Service
@Transactional
public class PlayCardService {

    public static List<PlayCard> constructDummyCards() {
        List<PlayCard> cardList = new ArrayList<PlayCard>();

        // All Bang Cards
        cardList.add(new BrownCard(Card.BANG, Rank.FOUR, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.EIGHT, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.QUEEN, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.TWO, Suit.CLUBS));
        cardList.add(new BrownCard(Card.BANG, Rank.ACE, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.KING, Suit.HEARTS));
        cardList.add(new BrownCard(Card.BANG, Rank.NINE, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.QUEEN, Suit.HEARTS));
        cardList.add(new BrownCard(Card.BANG, Rank.SIX, Suit.CLUBS));
        cardList.add(new BrownCard(Card.BANG, Rank.NINE, Suit.CLUBS));
        cardList.add(new BrownCard(Card.BANG, Rank.KING, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.FIVE, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.THREE, Suit.CLUBS));
        cardList.add(new BrownCard(Card.BANG, Rank.JACK, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.FIVE, Suit.CLUBS));
        cardList.add(new BrownCard(Card.BANG, Rank.FOUR, Suit.CLUBS));
        cardList.add(new BrownCard(Card.BANG, Rank.EIGHT, Suit.CLUBS));
        cardList.add(new BrownCard(Card.BANG, Rank.SEVEN, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.SIX, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.ACE, Suit.SPADES));
        cardList.add(new BrownCard(Card.BANG, Rank.TEN, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.SEVEN, Suit.CLUBS));
        cardList.add(new BrownCard(Card.BANG, Rank.TWO, Suit.DIAMONDS));
        cardList.add(new BrownCard(Card.BANG, Rank.ACE, Suit.HEARTS));
        cardList.add(new BrownCard(Card.BANG, Rank.THREE, Suit.DIAMONDS));

        return cardList;

    }
}
