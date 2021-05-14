package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public class Carabine extends Weapon {
    public Carabine() {
    }

    public Carabine(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.CARABINE;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player targetPlayer, PayLoadDTO payload) {
        usingPlayer.setRange(usingPlayer.getRange() + 3);
    }

    @Override
    public void onRemoval(Player affectedPlayer) {
        affectedPlayer.setRange(affectedPlayer.getBaseRange());
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}
