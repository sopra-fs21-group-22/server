package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        if (playCards == null) {
            return new ArrayList<>();
        }
        return playCards;
    }

    public void setPlayCards(List<PlayCard> playCards) {
        this.playCards = playCards;
    }

    public void removeCard(PlayCard card) {
        playCards.remove(card);
    }

    public PlayCard removeRandomCard() {
        if (playCards.size() == 0) {
            throw new GameLogicException("Player doesn't have any hand cards!");
        }
        int idx = (int) Math.random() * playCards.size();
        return playCards.remove(idx);
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
            addCardInOrder(card); // makes sure cards are added in order of priority
        }
    }

    public void addCard(PlayCard newCard) {
        if (playCards == null) {
            playCards = new ArrayList<>();
        }
        addCardInOrder(newCard);
    }

    private class SortByPriority implements Comparator<PlayCard> {

        @Override
        public int compare(PlayCard o1, PlayCard o2) {
            List<Priority> priorities = Arrays.asList(Priority.values());
            return priorities.indexOf(o1.getPriority()) - priorities.indexOf(o2.getPriority());
        }
    }

    /**
     * This function makes sure that the hand cards are added in the order of their
     * priority. The first card (index 0) has the highest priority and the last card
     * the lowest. The cards with the same priority are in arbitrary order.
     *
     * @param PlayCard
     */

    public void addCardInOrder(PlayCard card) {
        // TODO depending on how many priorities there will be --> loop over priorities
        // instead of if else
        playCards.add(card);
        Collections.sort(playCards, new SortByPriority());
    }
}