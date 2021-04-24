package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
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
        playCards.remove(cardToRemove);
    }

    public void addCards(List<PlayCard> newCards) {
        this.playCards.addAll(newCards);
    }
}