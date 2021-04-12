package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.entity.OrangeCard;
import ch.uzh.ifi.hase.soprafs21.entity.PlayCard;



@Service
@Transactional
public class DeckService {
    
    @Autowired
    DeckRepository deckRepository;

    public static void addCards(Deck deck){

        List<PlayCard> playCards = new ArrayList<PlayCard>();

        // here we add the 80 cards
        //example:
        OrangeCard card1 = new OrangeCard();
        playCards.add(card1);
        //end of example

        deck.setPlayCards(playCards);
    }

}
