package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

@Entity
public class Schofield extends BlueCard {

    public Schofield() {
    }

    public Schofield(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.SCHOFIELD;
    }

    @Override
    public void use(Player usingPlayer, Player targetPlayer) {
        usingPlayer.setRange(2);
        usingPlayer.getOnFieldCards().addOnFieldCard(this);
    }

    @Override
    public void onTurnStart(Player affectedPlayer) {
        // Does nothing on turn start
        return;
    }

    @Override
    public void undo(Player affectedPlayer) {
        affectedPlayer.setRange(1);

    }

    @Override
    public void onHit(Player affectedPlayer) {
        // Does nothing on hit
        return;
    }

}
