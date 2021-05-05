package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public abstract class BlueCard extends PlayCard {

    @Override
    public String getColor() {
        return "blue";
    }

    @Override
    public final void use(Player usingPlayer, Player target, PayLoadDTO payload) {
        super.use(usingPlayer, target, payload);
        Player targetPlayer = usingPlayer;
        targetPlayer.getOnFieldCards().addOnFieldCard(this);
    }

    /**
     * Runs every time the card owner starts his turn.
     *
     * @param affectedPlayer
     */
    public void onTurnStart(Player affectedPlayer) {
        // Default behaviour is to do nothing
    }

    /**
     * Runs when the card is removed from the user field
     *
     * @param affectedPlayer
     */
    public void onRemoval(Player affectedPlayer) {
        // Default behaviour is to do nothing
    }

}