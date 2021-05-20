package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public class Appaloosa extends Horse {

    public Appaloosa() {
    }

    public Appaloosa(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.APPALOOSA;
        this.priority = Priority.FOURTEENTH;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player targetPlayer, PayLoadDTO payload) {
        targetPlayer.setDistanceDecreaseToOthers(targetPlayer.getDistanceDecreaseToOthers() + 1);
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }

    @Override
    public void onRemoval(Player affectedPlayer) {
        affectedPlayer.setDistanceDecreaseToOthers(affectedPlayer.getDistanceDecreaseToOthers() - 1);
    }

}
