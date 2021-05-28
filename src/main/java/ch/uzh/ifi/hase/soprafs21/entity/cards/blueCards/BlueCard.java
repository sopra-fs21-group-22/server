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
    public void use(Player usingPlayer, Player target, PayLoadDTO payload) {
        super.use(usingPlayer, target, payload);

        List<BlueCard> fieldCards = target.getOnFieldCards().getOnFieldCards();
        for (BlueCard card : fieldCards) {
            if (card.getCard() == this.card) {
                throw new GameLogicException("A card with the same name is already on the field!");
            }
        }
        target.getOnFieldCards().addOnFieldCard(this);
        onPlacement(usingPlayer, target, payload);
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