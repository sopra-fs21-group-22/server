package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;

@Entity
public abstract class BlueCard extends PlayCard {

    @Override
    public String getColor() {
        return "blue";
    }

    @Override
    public final void use(Player usingPlayer, List<Player> targets) {
        super.use(usingPlayer, targets);
        if (targets.size() > 1) {
            throw new GameLogicException("A blue card can only be played on one player.");
        }
        if (targets.isEmpty()) {
            useOnce(usingPlayer, usingPlayer);
        } else {
            useOnce(usingPlayer, targets.get(0));
        }

    }

    protected abstract void useOnce(Player usingPlayer, Player targetPlayer);

    /**
     * Runs every time the user starts his turn
     * 
     * @param affectedPlayer
     */
    public void onTurnStart(Player affectedPlayer) {
        // Default behaviour is to do nothing
    }

    /**
     * Runs when the card leaves the user
     * 
     * @param affectedPlayer
     */
    public void undo(Player affectedPlayer) {
        // Default behaviour is to do nothing
    }

    /**
     * Runs when the player having this card gets hit
     * 
     * @param affectedPlayer
     * @return True if the hit has been absorbed false otherwise
     */
    public boolean takeHit(Player affectedPlayer) {
        return false;
    }
}