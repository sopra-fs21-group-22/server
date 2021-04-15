package ch.uzh.ifi.hase.soprafs21.service;

import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import ch.uzh.ifi.hase.soprafs21.repository.OnFieldCardsRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;




@Service
@Transactional
public class OnFieldCardsService {

    OnFieldCardsRepository onFieldCardsRepository;

    DeckRepository deckRepository;

    public OnFieldCards createOnFieldCards(){
        OnFieldCards onFieldCards = new OnFieldCards();

        onFieldCardsRepository.save(onFieldCards);

        return onFieldCards;
    }

    public void discardCard(BlueCard cardToDiscard, OnFieldCards onFieldCards, Deck discardPile){

        onFieldCards.removeOnFieldCard(cardToDiscard);
        onFieldCardsRepository.save(onFieldCards);

        List<PlayCard> discardPileCards = discardPile.getPlayCards();
        discardPileCards.add(0, cardToDiscard);
        discardPile.setPlayCards(discardPileCards);
        deckRepository.save(discardPile);
    }

    /*
    public void addCardToOnFieldCards(BlueCard cardToPlace, Hand hand, OnFieldCards onFieldCards){

        List<PlayCard> handCards = hand.getPlayCards();
        hand.remove(cardToPlace);
        hand.setPlayCards(handCards);
        handRepository.save(hand);

        onFieldCards.addOnFieldCard(cardToPlace);
        onFieldCardsRepository.save(hand);
    }


     */
}
