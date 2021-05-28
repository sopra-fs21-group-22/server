package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public class Winchester extends Weapon {
    public Winchester() {
    }

    public Winchester(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.WINCHESTER;
        this.priority = Priority.TWENTIETH;

    }

    @Override
    protected void onPlacement(Player usingPlayer, Player targetPlayer, PayLoadDTO payload) {
        usingPlayer.setRange(usingPlayer.getRange() + 4);
    }

    @Override
    public void onRemoval(Player affectedPlayer) {
        affectedPlayer.setRange(affectedPlayer.getRange() - 4);
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}
