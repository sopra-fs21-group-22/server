package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

@Entity
public class Volcanic extends BlueCard {
    public Volcanic() {
    }

    public Volcanic(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.VOLCANIC;
    }

    @Override
    protected void useOnce(Player usingPlayer, Player targetPlayer) {
        usingPlayer.setStillPlayableBangsThisRound(100);
    }

    @Override
    public void onTurnStart(Player affectedPlayer) {
        affectedPlayer.setStillPlayableBangsThisRound(100);
    }

    @Override
    public void undo(Player affectedPlayer) {
        affectedPlayer.setStillPlayableBangsThisRound(1);
    }
}
