package ch.uzh.ifi.hase.soprafs21.entity.cards;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class PlayCard {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Suit suit;

    @Column
    private Rank rank;

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
        return this.getClass().getSimpleName();
    }
}
