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
    protected void onPlacement(Player usingPlayer, Player target) {
        usingPlayer.setRange(2);
        usingPlayer.getOnFieldCards().addOnFieldCard(this);
    }

    @Override
    public void onRemoval(Player affectedPlayer) {
        affectedPlayer.setRange(1);

    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}