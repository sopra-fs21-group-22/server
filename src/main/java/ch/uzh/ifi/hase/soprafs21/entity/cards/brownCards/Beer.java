package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public class Beer extends BrownCard {

    public Beer(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.BEER;
        this.priority = Priority.THIRD;
    }

    public Beer() {
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload) {
        usingPlayer.setBullets(usingPlayer.getBullets() + 1);
    }

    @Override
    public boolean onBang(Player affectedPlayer) {
        if (affectedPlayer.getBullets() == 0) {
            onPlacement(affectedPlayer, affectedPlayer, null);
            return true;
        }
        return false;
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        if (usingPlayer.getBullets().equals(usingPlayer.getMaxBullets())) {
            throw new GameLogicException("Player is already at max health!");
        }
        return true;
    }
}
