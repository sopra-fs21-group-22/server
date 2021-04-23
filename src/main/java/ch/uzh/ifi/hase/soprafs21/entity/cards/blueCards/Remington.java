package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

@Entity
public class Remington extends BlueCard {

    public Remington() {

    }

    public Remington(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.REMINGTON;
    }

    @Override
    protected void useOnce(Player usingPlayer, Player target) {
        usingPlayer.setRange(3);
    }

    @Override
    public void undo(Player affectedPlayer) {
        affectedPlayer.setRange(1);
    }
}
