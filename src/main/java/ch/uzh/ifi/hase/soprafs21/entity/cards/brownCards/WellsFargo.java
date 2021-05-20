package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

import javax.persistence.Entity;

@Entity
public class WellsFargo extends BrownCard {

    public WellsFargo() {
    }

    public WellsFargo(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.WELLSFARGO;
        this.priority = Priority.THIRTEENTH;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload) {
        Hand userHand = usingPlayer.getHand();
        List<PlayCard> cards = usingPlayer.getTable().getDeck().drawCards(3);
        userHand.addCards(cards);
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }

}
