package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class Jail extends BlueCard {

    public Jail() {
    }

    public Jail(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.JAIL;
    }

    @Override
    protected void useOnce(Player usingPlayer, Player target) {
        if (target.getGameRole() != GameRole.SHERIFF) {
            target.getOnFieldCards().addOnFieldCard(this);
        } else {
            throw new GameLogicException("You can't put the Sheriff in Jail!");
        }
    }

    @Override
    public void onTurnStart(Player affectedPlayer) {
        // TODO
    }

    @Override
    public void undo(Player affectedPlayer) {
        affectedPlayer.getOnFieldCards().removeJailCard();
    }
}
