package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Entity
public class Barrel extends BlueCard {
    public Barrel() {
    }

    public Barrel(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.BARREL;
    }

    @Override
    protected void useOnce(Player usingPlayer, Player targetPlayer) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean takeHit(Player affectedPlayer) {
        PlayCard card = affectedPlayer.getTable().getDeck().drawCards(1).get(0);
        return card.getSuit().equals(Suit.HEARTS);
    }
}
