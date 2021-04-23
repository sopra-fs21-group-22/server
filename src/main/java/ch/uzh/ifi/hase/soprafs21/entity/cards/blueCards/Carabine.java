package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

@Entity
public class Carabine extends BlueCard {
    public Carabine() {
    }

    public Carabine(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.CARABINE;
    }

    @Override
    protected void useOnce(Player usingPlayer, Player targetPlayer) {
        usingPlayer.setRange(4);
    }

    @Override
    public void undo(Player affectedPlayer) {
        affectedPlayer.setRange(1);
    }
}
