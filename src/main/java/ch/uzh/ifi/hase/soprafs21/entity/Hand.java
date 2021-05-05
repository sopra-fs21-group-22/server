package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

/**
 * Hand has all the cards that a player has on his*her hand in order of
 * priority.
 */
@Entity
public class Hand {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hand_id")
    private List<PlayCard> playCards;

    public PlayCard get(int index) {
        return playCards.get(index);
    }

    public PlayCard getCardById(Long cardId) {
        for (PlayCard card : playCards) {
            if (card.getId().equals(cardId)) {
                return card;
            }
        }
        throw new GameLogicException(String.format("Player doesn't have a card with id %s in his hand.", cardId));
    }

    public Integer getLength() {
        return playCards == null ? 0 : playCards.size();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PlayCard> getPlayCards() {
        List<PlayCard> inOrder = checkOrder(playCards);
        return inOrder;
    }

    public void setPlayCards(List<PlayCard> playCards) {
        List<PlayCard> inOrder = checkOrder(playCards);
        this.playCards = inOrder;
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

    public void addCards(List<PlayCard> newCards) {
        if (playCards == null) {
            playCards = new ArrayList<>();
        }
        for (PlayCard card : newCards) {
            card.addCardInOrder(playCards); // makes sure cards are added in order of priority
        }
    }

    public List<PlayCard> checkOrder(List<PlayCard> playCards) {
        List<PlayCard> inOrder = new ArrayList<>();
        for (int i = 0; i < playCards.size(); i++) {
            playCards.get(i).addCardInOrder(inOrder);
        }
        return inOrder;
    }
}