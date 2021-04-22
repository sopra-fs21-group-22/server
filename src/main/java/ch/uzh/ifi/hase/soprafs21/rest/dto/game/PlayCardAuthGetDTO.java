package ch.uzh.ifi.hase.soprafs21.rest.dto.game;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;

public class PlayCardAuthGetDTO extends PlayCardGetDTO {
    @Override
    public Suit getSuit() {
        return this.suit;
    }

    @Override
    public Rank getRank() {
        return this.rank;
    }

    @Override
    public Card getCard() {
        return this.card;
    }

}
