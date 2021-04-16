package ch.uzh.ifi.hase.soprafs21.entity.cards;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class PlayCard {

    @Id
    @GeneratedValue
    protected Long id;

    @Column
    protected Suit suit;

    @Column
    protected Rank rank;

    @Column
    protected String color;

    @Column
    protected Card card;

    protected PlayCard() {

    }

    protected PlayCard(Card card, Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return this.card.toString();
    }
}
