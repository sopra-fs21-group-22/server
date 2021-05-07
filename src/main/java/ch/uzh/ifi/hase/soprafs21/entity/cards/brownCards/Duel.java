package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

public class Duel extends BrownCard {

    public Duel() {
    }

    public Duel(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.DUEL;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload) {
        usingPlayer.getTable().setGameStatus(GameStatus.DUEL);
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return !usingPlayer.getId().equals(targetPlayer.getId());
    }

}
