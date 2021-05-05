package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;
import java.util.Random;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

public class Panic extends BrownCard {
    public Panic() {
    }

    public Panic(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.PANIC;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload) {
        Long id = payload.getTargetCardId();
        PlayCard cardToAdd;
        if (id == null) {
            cardToAdd = target.getHand().removeRandomCard();
            usingPlayer.getHand().addCard(cardToAdd);
        } else {
            cardToAdd = target.getOnFieldCards().removeOnFieldCard(id);
            usingPlayer.getHand().addCard(cardToAdd);
        }
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.reachesWithDistance(targetPlayer, 1);
    }

}
