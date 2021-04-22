package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

import javax.persistence.Entity;

@Entity
public class Dynamite extends BlueCard{
    public Dynamite(){
    }

    public Dynamite(Rank rank, Suit suit){
        this.rank = rank;
        this.suit = suit;
        this.card = Card.DYNAMITE;
    }

    @Override
    public void use(Player usingPlayer, Player targetPlayer) {
        targetPlayer.getOnFieldCards().addOnFieldCard(this);
    }

    @Override
    public void onTurnStart(Player affectedPlayer) {
    }

    @Override
    public void undo(Player affectedPlayer) {
        affectedPlayer.getOnFieldCards().removeDynamiteCard();
    }

    @Override
    public void onHit(Player affectedPlayer) {
        // Does nothing on hit
        return;
    }
}
