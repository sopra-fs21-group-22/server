package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

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
    public boolean onBang(Player affectedPlayer) {
        affectedPlayer.getHand().removeCard(this);
        affectedPlayer.getTable().getDiscardPile().addCard(this);
        return true;
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return true;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload) {
        throw new GameLogicException(
                "Can't be placed! This card will automatically be used once another player plays a Bang against the this cards owner!");
    }

}
