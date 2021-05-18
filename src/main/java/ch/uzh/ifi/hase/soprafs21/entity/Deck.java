package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.*;

import javax.persistence.*;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;

@Entity
public class Deck {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "deck_id")
    private List<PlayCard> playCards;

    @OneToOne(cascade = CascadeType.ALL)
    private Deck discardPile;

    public List<PlayCard> drawCards(int amount) {
        if (amount > this.playCards.size()) {
            shuffle();
        }
        List<PlayCard> drawnCards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            drawnCards.add(this.playCards.remove(0));
        }
        return drawnCards;
    }

    private void shuffle() {
        List<PlayCard> pileCards = discardPile.getPlayCards();
        List<PlayCard> toBeAddedCards = pileCards.subList(1, pileCards.size());
        List<PlayCard> topCard = pileCards.subList(0, 1);
        discardPile.setPlayCards(topCard);
        Collections.shuffle(toBeAddedCards);
        this.playCards.addAll(toBeAddedCards);
    }

    public void addCard(PlayCard card) {
        if (this.playCards == null) {
            this.playCards = new ArrayList<>();
        }
        this.playCards.add(0, card);
    }

    public void addCards(List<PlayCard> list) {
        if (this.playCards == null) {
            this.playCards = new ArrayList<>();
        }
        this.playCards.addAll(list);
    }

    public Deck getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(Deck discardPile) {
        this.discardPile = discardPile;
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
}