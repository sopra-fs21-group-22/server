package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

@Entity
public abstract class BrownCard extends PlayCard {

    @Override
    public String getColor() {
        return "brown";
    }

    // adding checks applying to brown cards
    @Override
    public final void use(Player usingPlayer, List<Player> targets) {
        super.use(usingPlayer, targets);
        for (Player target : targets) {
            if (target.getBullets() <= 0) {
                throw new GameLogicException("Target Player is already dead. Please don't attack corpses!");
            }
        }
        if (usingPlayer.getBullets() <= 0) {
            throw new GameLogicException("Card user is already dead. Corpses can't play anymore!");
        }
        onPlacement(usingPlayer, targets);
        usingPlayer.getTable().getDiscardPile().addCard(this);
    }

    protected abstract void onPlacement(Player usingPlayer, List<Player> targets);

}