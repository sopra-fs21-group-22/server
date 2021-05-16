package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Appaloosa;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Barrel;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Carabine;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Dynamite;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Jail;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Mustang;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Remington;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.*;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Schofield;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Volcanic;

@Service
@Transactional
public class PlayCardService {

    public static List<PlayCard> getPlayCards() {
        List<PlayCard> cardList = new ArrayList<>();

        // Weapon cards
        cardList.add(new Schofield(Rank.QUEEN, Suit.CLUBS));
        cardList.add(new Schofield(Rank.KING, Suit.SPADES));
        cardList.add(new Schofield(Rank.JACK, Suit.CLUBS));

        cardList.add(new Remington(Rank.KING, Suit.CLUBS));

        cardList.add(new Carabine(Rank.ACE, Suit.CLUBS));

        cardList.add(new Volcanic(Rank.TEN, Suit.CLUBS));
        cardList.add(new Volcanic(Rank.TEN, Suit.SPADES));

        cardList.add(new Mustang(Rank.EIGHT, Suit.HEARTS));
        cardList.add(new Mustang(Rank.NINE, Suit.HEARTS));

        cardList.add(new Appaloosa(Rank.ACE, Suit.SPADES));

        cardList.add(new Panic(Rank.EIGHT, Suit.DIAMONDS));
        cardList.add(new Panic(Rank.ACE, Suit.HEARTS));
        cardList.add(new Panic(Rank.JACK, Suit.HEARTS));
        cardList.add(new Panic(Rank.QUEEN, Suit.HEARTS));

        cardList.add(new CatBalou(Rank.JACK, Suit.DIAMONDS));
        cardList.add(new CatBalou(Rank.NINE, Suit.DIAMONDS));
        cardList.add(new CatBalou(Rank.TEN, Suit.DIAMONDS));
        cardList.add(new CatBalou(Rank.KING, Suit.HEARTS));

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
        // cardList.add(new Bang(Rank.NINE, Suit.CLUBS)); //card doesnt exist in
        // frontend
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

        // All Missed Cards
        cardList.add(new Missed(Rank.TWO, Suit.SPADES));
        cardList.add(new Missed(Rank.THREE, Suit.SPADES));
        cardList.add(new Missed(Rank.FOUR, Suit.SPADES));
        cardList.add(new Missed(Rank.FIVE, Suit.SPADES));
        cardList.add(new Missed(Rank.SIX, Suit.SPADES));
        cardList.add(new Missed(Rank.SEVEN, Suit.SPADES));
        cardList.add(new Missed(Rank.EIGHT, Suit.SPADES));
        cardList.add(new Missed(Rank.TEN, Suit.CLUBS));
        cardList.add(new Missed(Rank.JACK, Suit.CLUBS));
        cardList.add(new Missed(Rank.QUEEN, Suit.CLUBS));
        cardList.add(new Missed(Rank.KING, Suit.CLUBS));
        cardList.add(new Missed(Rank.ACE, Suit.CLUBS));

        cardList.add(new Beer(Rank.SEVEN, Suit.HEARTS));
        cardList.add(new Beer(Rank.SIX, Suit.HEARTS));
        cardList.add(new Beer(Rank.TEN, Suit.HEARTS));
        cardList.add(new Beer(Rank.NINE, Suit.HEARTS));
        cardList.add(new Beer(Rank.EIGHT, Suit.HEARTS));
        cardList.add(new Beer(Rank.JACK, Suit.HEARTS));

        cardList.add(new Saloon(Rank.FIVE, Suit.HEARTS));

        cardList.add(new Gatling(Rank.TEN, Suit.HEARTS));

        cardList.add(new Indians(Rank.ACE, Suit.DIAMONDS));
        cardList.add(new Indians(Rank.KING, Suit.DIAMONDS));

        cardList.add(new WellsFargo(Rank.THREE, Suit.HEARTS));
        cardList.add(new StageCoach(Rank.NINE, Suit.SPADES));
        cardList.add(new StageCoach(Rank.NINE, Suit.SPADES));
        // TODO fix general store and readd
        // cardList.add(new GeneralStore(Rank.NINE, Suit.CLUBS));
        // cardList.add(new GeneralStore(Rank.QUEEN, Suit.SPADES));

        cardList.add(new Jail(Rank.JACK, Suit.SPADES));
        cardList.add(new Jail(Rank.TEN, Suit.SPADES));
        cardList.add(new Jail(Rank.FOUR, Suit.HEARTS));

        cardList.add(new Barrel(Rank.KING, Suit.SPADES));
        cardList.add(new Barrel(Rank.QUEEN, Suit.SPADES));

        cardList.add(new Dynamite(Rank.TWO, Suit.HEARTS));

        Collections.shuffle(cardList);

        return cardList;
    }
}
