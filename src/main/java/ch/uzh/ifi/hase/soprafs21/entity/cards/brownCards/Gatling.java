package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

@Entity
public class Gatling extends BrownCard {
    public Gatling() {
    }

    public Gatling(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.GATLING;
    }

    @Override
    protected void useOnce(Player usingPlayer, List<Player> targets) {
        for (int i = 0; i < targets.size(); i++) {
            Player target = targets.get(i);
            target.setBullets(target.getBullets() - 1);
        }
    }
}
