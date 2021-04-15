package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.HandRepository;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.service.PlayCardService;




@Service
@Transactional
public class HandService {
    
    @Autowired
    HandRepository handRepository;

    @Autowired 
    DeckRepository deckRepository;

    public  Hand createHand(){
        Hand hand = new Hand();

        handRepository.save(hand);

        return hand;
    }

    public void putOnDiscardPile(PlayCard playCard, Hand hand, Deck discardPile){

        List<PlayCard> handCards = hand.getPlayCards();
        handCards.remove(playCard);
        hand.setPlayCards(handCards);
        handRepository.save(hand);

        List<PlayCard> discardPileCards = discardPile.getPlayCards();
        discardPileCards.add(0, playCard);
        discardPile.setPlayCards(discardPileCards);
        deckRepository.save(discardPile);
    }

    public void drawCard(PlayCard playCard, Hand hand, Deck deck){
        List<PlayCard> deckCards = deck.getPlayCards();
        deckCards.remove(playCard);
        deck.setPlayCards(deckCards);
        deckRepository.save(deck);


        List<PlayCard> handCards = hand.getPlayCards();
        handCards.add(playCard);
        hand.setPlayCards(handCards);
        handRepository.save(hand);

    }

}
