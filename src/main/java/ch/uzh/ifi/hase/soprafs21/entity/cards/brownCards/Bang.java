package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

@Entity
public class Bang extends BrownCard {

    public Bang(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.BANG;
    }

    public Bang() {
    }

    @Override
    protected void useOnce(Player usingPlayer, List<Player> targets) {
        if (targets.size() != 1) {
            throw new GameLogicException("A BANG card can only hit one target!");
        }

        if (usingPlayer.getStillPlayableBangsThisRound() <= 0) {
            throw new GameLogicException("Can't play more BANG cards this round!");
        }
        Player target = targets.get(0);
        target.setBullets(target.getBullets() - 1);
        usingPlayer.setStillPlayableBangsThisRound(usingPlayer.getStillPlayableBangsThisRound() - 1);
    }
}
