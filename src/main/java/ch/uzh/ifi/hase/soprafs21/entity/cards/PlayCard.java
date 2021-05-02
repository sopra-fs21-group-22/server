package ch.uzh.ifi.hase.soprafs21.entity.cards;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

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

    @Column
    protected Priority priority;

    /**
     * Meant to be run only once when the card is used by the user
     *
     * @param usingPlayer
     * @param targets
     */
    public void use(Player usingPlayer, List<Player> targets) {
        for (Player target : targets) {
            if (target.getBullets() <= 0) {
                throw new GameLogicException("Target Player is already dead. Please don't attack corpses!");
            }
        }
        if (usingPlayer.getBullets() <= 0) {
            throw new GameLogicException("Card user is already dead. Corpses can't play anymore!");
        }
    }

    /**
     * Runs when the player having this card gets hit by a bang card
     *
     * @param affectedPlayer
     * @return True if the hit has been absorbed false otherwise
     */
    public boolean onBang(Player affectedPlayer) {
        return false;
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
