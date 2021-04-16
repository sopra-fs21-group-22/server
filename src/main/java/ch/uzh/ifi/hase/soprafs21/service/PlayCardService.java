package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;

@Service
@Transactional
public class PlayCardService {

    public static List<PlayCard> constructDummyCards() {
        List<PlayCard> cardList = new ArrayList<PlayCard>();

        PlayCard ocard1 = new Bang();
        cardList.add(ocard1);

        PlayCard ocard2 = new Bang();
        cardList.add(ocard2);

        PlayCard ocard3 = new Bang();
        cardList.add(ocard3);

        PlayCard ocard4 = new Bang();
        cardList.add(ocard4);

        PlayCard ocard5 = new Bang();
        cardList.add(ocard5);

        PlayCard ocard6 = new Bang();
        cardList.add(ocard6);

        PlayCard ocard7 = new Bang();
        cardList.add(ocard7);

        PlayCard ocard8 = new Bang();
        cardList.add(ocard8);

        PlayCard ocard9 = new Bang();
        cardList.add(ocard9);

        PlayCard ocard10 = new Bang();
        cardList.add(ocard10);

        PlayCard card1 = new Bang();
        cardList.add(card1);

        PlayCard card2 = new Bang();
        cardList.add(card2);

        PlayCard card3 = new Bang();
        cardList.add(card3);

        PlayCard card4 = new Bang();
        cardList.add(card4);

        PlayCard card5 = new Bang();
        cardList.add(card5);

        PlayCard card6 = new Bang();
        cardList.add(card6);

        PlayCard card7 = new Bang();
        cardList.add(card7);

        PlayCard card8 = new Bang();
        cardList.add(card8);
        // PlayCard bcard1 = new Bang();
        // cardList.add(bcard1);

        // PlayCard bcard2 = new Bang();
        // cardList.add(bcard2);

        // PlayCard bcard3 = new Bang();
        // cardList.add(bcard3);

        return cardList;

    }
}
