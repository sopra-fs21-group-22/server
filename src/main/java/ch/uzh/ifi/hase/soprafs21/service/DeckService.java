package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.entity.OrangeCard;
import ch.uzh.ifi.hase.soprafs21.entity.BlueCard;
import ch.uzh.ifi.hase.soprafs21.entity.PlayCard;
import ch.uzh.ifi.hase.soprafs21.service.PlayCardService;




@Service
@Transactional
public class DeckService {
    
    @Autowired
    DeckRepository deckRepository;

    public void fill(Deck deck){

        List<PlayCard> playCards = PlayCardService.constructDummyCards();

        deck.setPlayCards(playCards);

        deckRepository.save(deck);
    }

    public Deck createDeck(){
        Deck deck = new Deck();

        deckRepository.save(deck);

        return deck;
    }

    public Deck createDiscardPile(){
        Deck deck = new Deck();

        deckRepository.save(deck);

        return deck;
    }

    public void shuffle(Deck deck, Deck discardPile){
        List<PlayCard> topCard = discardPile.getPlayCards();
        List<PlayCard> discardCards = discardPile.getPlayCards();

        topCard = topCard.subList(0, 1);      
        discardCards.remove(0);
        discardCards = this.randomizeCards(discardCards);
        
        discardPile.setPlayCards(topCard);
        deckRepository.save(discardPile);
        deck.setPlayCards(discardCards);
        deckRepository.save(deck);

    }

    public List<PlayCard> randomizeCards(List<PlayCard> playCards) {
        Collections.shuffle(playCards);
        return playCards;
    }


}
