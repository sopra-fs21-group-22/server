package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Schofield;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.BrownCard;

@Service
@Transactional
public class PlayCardService {

    public static List<PlayCard> getPlayCards() {
        List<PlayCard> cardList = new ArrayList<>();

        // All Schofield Cards
        cardList.add(new Schofield(Rank.QUEEN, Suit.CLUBS));
        cardList.add(new Schofield(Rank.KING, Suit.SPADES));
        cardList.add(new Schofield(Rank.JACK, Suit.CLUBS));

        // All Bang Cards
        cardList.add(new Bang(Rank.FOUR, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.EIGHT, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.QUEEN, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.TWO, Suit.CLUBS));
        cardList.add(new Bang(Rank.ACE, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.KING, Suit.HEARTS));
        cardList.add(new Bang(Rank.NINE, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.QUEEN, Suit.HEARTS));
        cardList.add(new Bang(Rank.SIX, Suit.CLUBS));
        cardList.add(new Bang(Rank.NINE, Suit.CLUBS));
        cardList.add(new Bang(Rank.KING, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.FIVE, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.THREE, Suit.CLUBS));
        cardList.add(new Bang(Rank.JACK, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.FIVE, Suit.CLUBS));
        cardList.add(new Bang(Rank.FOUR, Suit.CLUBS));
        cardList.add(new Bang(Rank.EIGHT, Suit.CLUBS));
        cardList.add(new Bang(Rank.SEVEN, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.SIX, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.ACE, Suit.SPADES));
        cardList.add(new Bang(Rank.TEN, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.SEVEN, Suit.CLUBS));
        cardList.add(new Bang(Rank.TWO, Suit.DIAMONDS));
        cardList.add(new Bang(Rank.ACE, Suit.HEARTS));
        cardList.add(new Bang(Rank.THREE, Suit.DIAMONDS));

        return cardList;

    }
}
