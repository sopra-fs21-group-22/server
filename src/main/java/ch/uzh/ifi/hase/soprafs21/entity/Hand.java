package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
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
    private List<PlayCard> playCards = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;

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
        return playCards;
    }

    public void setPlayCards(List<PlayCard> playCards) {
        playCards.sort(new SortByPriority());
        this.playCards = playCards;
        this.playCards.sort(new SortByPriority());
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

    public PlayCard removeCardById(Long cardId) {
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
        return cardToRemove;
    }

    public void removeAllCards() {
        this.playCards = new ArrayList<>();
    }

    public void addCards(List<PlayCard> newCards) {
        if (playCards == null) {
            playCards = new ArrayList<>();
        }
        for (PlayCard card : newCards) {
            addCard(card);
        }
        playCards.sort(new SortByPriority());
    }

    public void addCard(PlayCard newCard) {
        if (playCards == null) {
            playCards = new ArrayList<>();
        }
        addCardInOrder(newCard);
    }

    /*
     * in case anyone cares the order of all the cards is: Dynamite, Jail, Missed,
     * Beer, Barrel, Bang, GeneralStore, CatBalou, Panic, Gatling, Indians,
     * StageCoach, WellsFargo, Appaloosa, Mustang, Volcanic, Schofield, Remington,
     * Carabine, and Winchester
     */
    public static class SortByPriority implements Comparator<PlayCard> {

        /**
         * Returns a negative integer, zero, or a positive integer as the first argument
         * is less than, equal to, or greater than the second. So if you compare a Bang
         * with priority Five and a Dynamite with priority One it will return -4 meaning
         * Bang is less than dynamite.
         */
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
     * @param
     */

    public void addCardInOrder(PlayCard card) {
        if (card.getCard().equals(Card.DYNAMITE)) {
            player.getOnFieldCards().addOnFieldCard((BlueCard) card);
        } else {
            playCards.add(card);
            playCards.sort(new SortByPriority());
        }

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}