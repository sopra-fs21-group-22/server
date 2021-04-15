package ch.uzh.ifi.hase.soprafs21.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.BrownCard;
import ch.uzh.ifi.hase.soprafs21.service.PlayCardService;

@Service
@Transactional
public class DeckService {

    @Autowired
    DeckRepository deckRepository;

    @Autowired
    PlayerTableRepository playerTableRepository;

    public void fill(Deck deck) {

        List<PlayCard> playCards = PlayCardService.constructDummyCards();

        deck.setPlayCards(playCards);

        deckRepository.save(deck);
        deckRepository.flush();

    }

    public Deck createDeck(){
        Deck deck = new Deck();

        deckRepository.save(deck);
        deckRepository.flush();


        return deck;
    }

    public Deck createDiscardPile(){
        Deck deck = new Deck();

        deckRepository.save(deck);
        deckRepository.flush();


        return deck;
    }

    public void shuffle(PlayerTable table){
        List<PlayCard> discardCards = table.getDiscardPile().getPlayCards();
        List<PlayCard> topCard = table.getDiscardPile().getPlayCards();


        topCard = topCard.subList(0, 1);      
        discardCards = discardCards.subList(1,discardCards.size());
        Collections.shuffle(discardCards);
        
        table.getDiscardPile().setPlayCards(topCard);
        table.getDeck().setPlayCards(discardCards);
        playerTableRepository.save(table);
        playerTableRepository.flush();

    }

    public void drawCards(PlayerTable table, Player player, Integer n) {
        for(int i = 0; i < n; i++) {   
            if (table.getDeck().getPlayCards().size() < 2){
                player.getHand().getPlayCards().add(table.getDeck().getPlayCards().get(0));
                table.getDeck().getPlayCards().remove(0);
                this.shuffle(table);
            }
            else {
                player.getHand().getPlayCards().add(table.getDeck().getPlayCards().get(0));
                table.getDeck().getPlayCards().remove(0);
            }
        }  
    }

    public void layCard(PlayerTable table, Player player, PlayCard playCard){
        Integer cardIndex = null;

        for(int i = 0; i < player.getHand().getPlayCards().size(); i++) {   
            if (player.getHand().getPlayCards().get(i) == playCard) {
                cardIndex=i;
            }
        }
        table.getDiscardPile().getPlayCards().add(0, player.getHand().getPlayCards().get(cardIndex));
        player.getHand().getPlayCards().remove(playCard);
    }


}
