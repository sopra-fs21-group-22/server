package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.OrangeCard;
import ch.uzh.ifi.hase.soprafs21.entity.BlueCard;


@Service
@Transactional
public class PlayCardService {
    
    public static List<PlayCard> constructDummyCards(){
        List<PlayCard> cardList = new ArrayList<PlayCard>(); 

        PlayCard ocard1 = new OrangeCard();
        cardList.add(ocard1);

        PlayCard ocard2 = new OrangeCard();
        cardList.add(ocard2);

        PlayCard ocard3 = new OrangeCard();
        cardList.add(ocard3);

        PlayCard ocard4 = new OrangeCard();
        cardList.add(ocard4);

        PlayCard ocard5 = new OrangeCard();
        cardList.add(ocard5);

        PlayCard bcard1 = new BlueCard();
        cardList.add(bcard1);

        PlayCard bcard2 = new BlueCard();
        cardList.add(bcard2);

        PlayCard bcard3 = new BlueCard();
        cardList.add(bcard3);

        return cardList;

    }
}
