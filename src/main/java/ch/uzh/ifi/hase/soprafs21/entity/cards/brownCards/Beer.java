package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

@Entity
public class Beer extends BrownCard {

    public Beer(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.BEER;
    }

    public Beer() {
    }

    @Override
    protected void onPlacement(Player usingPlayer, List<Player> targets) {
        if (usingPlayer.getBullets().equals(usingPlayer.getMaxBullets())) {
            throw new GameLogicException("Player is already at max health!");
        }
        usingPlayer.setBullets(usingPlayer.getBullets() + 1);
    }
}
