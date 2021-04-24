package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class Dynamite extends BlueCard {
    public Dynamite() {
    }

    public Dynamite(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.DYNAMITE;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player target) {
        target.getOnFieldCards().addOnFieldCard(this);
    }

    @Override
    public void onTurnStart(Player affectedPlayer) {
        //
    }

    @Override
    public void onRemoval(Player affectedPlayer) {
        affectedPlayer.getOnFieldCards().removeDynamiteCard();
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}
