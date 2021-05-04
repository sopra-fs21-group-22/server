package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Entity
public class Appaloosa extends BlueCard {

    public Appaloosa() {
    }

    public Appaloosa(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.APPALOOSA;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player targetPlayer) {
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
