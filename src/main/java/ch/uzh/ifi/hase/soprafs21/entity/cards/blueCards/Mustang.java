package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public class Mustang extends Horse {
    public Mustang() {
    }

    public Mustang(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.MUSTANG;
        this.priority = Priority.FIFTEENTH;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player targetPlayer, PayLoadDTO payload) {
        usingPlayer.setDistanceIncreaseForOthers(usingPlayer.getDistanceIncreaseForOthers() + 1);
    }

    @Override
    public void onRemoval(Player affectedPlayer) {
        affectedPlayer.setDistanceIncreaseForOthers(affectedPlayer.getDistanceIncreaseForOthers() - 1);
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}
