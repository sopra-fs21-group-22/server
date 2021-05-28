package ch.uzh.ifi.hase.soprafs21.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedDate;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Entity
public class Deck {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private PlayCard topCard;

    public PlayCard getTopCard() {
        return topCard;
    }

    public void setTopCard(PlayCard topCard) {
        this.topCard = topCard;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "deck_id")
    private List<PlayCard> playCards = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Deck discardPile;

    @Transient
    private final Logger logger = LoggerFactory.getLogger(Deck.class);

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
        this.topCard = card;
    }

    public void addCards(List<PlayCard> cards) {
        if (this.playCards == null) {
            this.playCards = new ArrayList<>();
        }
        for (PlayCard card : cards) {
            addCard(card);
        }
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