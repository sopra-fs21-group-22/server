package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Missed extends BrownCard {

    public Missed(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.MISSED;
        this.priority = Priority.SECOND;
    }

    public Missed() {
    }

    @Override
    protected void onPlacement(Player usingPlayer, List<Player> targets) {
        usingPlayer.setBullets(usingPlayer.getBullets() + 1);
    }

    @Override
    public boolean onBang(Player affectedPlayer){
        List<Player> targets = new ArrayList<>();
        onPlacement(affectedPlayer, targets);
        return true;
    }

}
