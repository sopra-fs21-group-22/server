package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

@Entity
public class Mustang extends BlueCard {
    public Mustang() {
    }

    public Mustang(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.MUSTANG;
    }

    @Override
    protected void useOnce(Player usingPlayer, Player targetPlayer) {
        usingPlayer.setDistanceIncreaseForOthers(usingPlayer.getDistanceIncreaseForOthers() + 1);
    }

    @Override
    public void undo(Player affectedPlayer) {
        affectedPlayer.setDistanceIncreaseForOthers(affectedPlayer.getDistanceIncreaseForOthers() - 1);
    }
}
