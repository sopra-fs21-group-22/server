package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.BrownCard;
import ch.uzh.ifi.hase.soprafs21.service.PlayCardService;

@Service
@Transactional
public class DeckService {

    @Autowired
    DeckRepository deckRepository;

    public void fill(Deck deck) {

        List<PlayCard> playCards = PlayCardService.constructDummyCards();

        deck.setPlayCards(playCards);
    }

}
