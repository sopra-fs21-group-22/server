package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

@Entity
public abstract class BlueCard extends PlayCard {

    @Override
    public String getColor() {
        return "blue";
    }

    @Override
    public final void use(Player usingPlayer, List<Player> targets) {
        super.use(usingPlayer, targets);
        Player targetPlayer = usingPlayer;
        if (targets.size() > 1) {
            throw new GameLogicException("A blue card can only be played on one player.");
        }
        if (targets.size() == 1) {
            targetPlayer = targets.get(0);
        }
        if (!targetIsValid(usingPlayer, targetPlayer)) {
            throw new GameLogicException(String.format("Target player is not valid for card %s.", this.card));
        }
        onPlacement(usingPlayer, targetPlayer);
        targetPlayer.getOnFieldCards().addOnFieldCard(this);
    }

    /**
     * Runs when the card is played. Responsible for placing the card correctly
     *
     * @param usingPlayer
     * @param targetPlayer
     */
    protected abstract void onPlacement(Player usingPlayer, Player targetPlayer);

    /**
     * Validates if the target is a valid target
     *
     * @param usingPlayer
     * @param targetPlayer
     */
    protected abstract boolean targetIsValid(Player usingPlayer, Player targetPlayer);

    /**
     * Runs every time the user starts his turn
     *
     * @param affectedPlayer
     */
    public void onTurnStart(Player affectedPlayer, PlayerTable table) {
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

    /**
     * Runs when the player having this card gets hit by a bang card
     *
     * @param affectedPlayer
     * @return True if the hit has been absorbed false otherwise
     */
    public boolean onBang(Player affectedPlayer) {
        return false;
    }
}