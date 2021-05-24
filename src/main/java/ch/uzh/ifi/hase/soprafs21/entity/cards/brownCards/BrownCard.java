package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.gameMoves.GameMove;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public abstract class BrownCard extends PlayCard {

    @Override
    public String getColor() {
        return "brown";
    }

    // handling brown cards
    @Override
    public final void use(Player usingPlayer, Player target, PayLoadDTO payload) {
        super.use(usingPlayer, target, payload);
        usingPlayer.getTable().getDiscardPile().addCard(this);
        onPlacement(usingPlayer, target, payload);
    }

    protected abstract void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload);
}