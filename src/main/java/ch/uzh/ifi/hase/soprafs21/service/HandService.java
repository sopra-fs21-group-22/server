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
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
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
