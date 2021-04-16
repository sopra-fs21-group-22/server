package ch.uzh.ifi.hase.soprafs21.service;

import java.util.*;

import ch.uzh.ifi.hase.soprafs21.entity.VisibleCards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Service
@Transactional
public class DeckService {

    @Autowired
    DeckRepository deckRepository;

    @Autowired
    PlayerTableRepository playerTableRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    VisibleCardsService visibleCardsService;


    public void fill(Deck deck) {

        List<PlayCard> playCards = PlayCardService.constructDummyCards();

        deck.setPlayCards(playCards);

        deckRepository.save(deck);
        deckRepository.flush();

    }

    public Deck createDeck(){
        Deck deck = new Deck();
        this.fill(deck);
        deckRepository.save(deck);
        deckRepository.flush();


        return deck;
    }

    public Deck createDiscardPile(){
        Deck deck = new Deck();
        List<PlayCard> playCards = new ArrayList<PlayCard>();

        deck.setPlayCards(playCards);
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

                playerTableRepository.save(table);
                playerTableRepository.flush();

                playerRepository.save(player);
                playerRepository.flush();
            }
            else {
                player.getHand().getPlayCards().add(table.getDeck().getPlayCards().get(0));
                table.getDeck().getPlayCards().remove(0);

                playerTableRepository.save(table);
                playerTableRepository.flush();

                playerRepository.save(player);
                playerRepository.flush();
            }
        }  
    }

    /**
     * A card is drawn from the deck, instead of putting it on a players hand it is placed onto
     * the Visible Cards, for everyone to see.
     */

    public void addCardToVisibleCards(PlayerTable table, Integer n) {
        VisibleCards visibleCards = visibleCardsService.createVisibleCards();
        for(int i = 0; i < n; i++) {
            if (table.getDeck().getPlayCards().size() < 2){
                visibleCards.addACard(table.getDeck().getPlayCards().get(0));
                table.getDeck().getPlayCards().remove(0);
                this.shuffle(table);

                playerTableRepository.save(table);
                playerTableRepository.flush();
            }
            else {
                visibleCards.addACard(table.getDeck().getPlayCards().get(0));
                table.getDeck().getPlayCards().remove(0);

                playerTableRepository.save(table);
                playerTableRepository.flush();
            }
        }
    }

    


}
