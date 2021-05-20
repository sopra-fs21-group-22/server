package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public class Volcanic extends Weapon {
    public Volcanic() {
    }

    public Volcanic(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.VOLCANIC;
        this.priority = Priority.SIXTEENTH;

    }

    @Override
    protected void onPlacement(Player usingPlayer, Player targetPlayer, PayLoadDTO payload) {
        usingPlayer.setStillPlayableBangsThisRound(100);
    }

    @Override
    public void onTurnStart(Player affectedPlayer) {
        affectedPlayer.setStillPlayableBangsThisRound(100);
    }

    @Override
    public void onRemoval(Player affectedPlayer) {
        affectedPlayer.setStillPlayableBangsThisRound(affectedPlayer.getPlaybleBangsAnyRound());
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}
