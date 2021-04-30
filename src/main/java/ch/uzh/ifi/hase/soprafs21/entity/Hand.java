package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

@Entity
public class Hand {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hand_id")
    private List<PlayCard> playCards;

    public PlayCard getCardById(Long cardId) {
        for (PlayCard card : playCards) {
            if (card.getId().equals(cardId)) {
                return card;
            }
        }
        throw new GameLogicException(String.format("Player doesn't have a card with id %s in his hand.", cardId));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PlayCard> getPlayCards() {
        return playCards;
    }

    public void setPlayCards(List<PlayCard> playCards) {
        this.playCards = playCards;
    }

    public void removeCard(PlayCard card) {
        playCards.remove(card);
    }

    public void removeCardById(Long cardId) {
        PlayCard cardToRemove = null;
        for (PlayCard card : playCards) {
            if (card.getId().equals(cardId)) {
                cardToRemove = card;
            }
        }
        if (cardToRemove == null) {
            throw new GameLogicException("Card to be removed doesn't exist!");
        }
        playCards.remove(cardToRemove);
    }

    /**
     * getCardsByCardType: returns all the cards in the hand which have a certain card type
     * e.g. getCardsByCardType(Card.MISSED) returns an ArrayList of all missed cards
     * @param card
     * @return
     */
    public List<PlayCard> getCardsByCardType(Card card){
        List<PlayCard> soughtForCard = new ArrayList<>();
        for (PlayCard cardInHand : playCards) {
            if (cardInHand.getCard() == card) {
                soughtForCard.add(cardInHand);
            }
        }
        return soughtForCard;
    }

    public void addCards(List<PlayCard> newCards) {
        this.playCards.addAll(newCards);
    }
}