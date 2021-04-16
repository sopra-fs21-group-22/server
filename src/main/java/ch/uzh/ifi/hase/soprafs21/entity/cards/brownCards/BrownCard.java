package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import javax.persistence.Column;
import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Entity
public class BrownCard extends PlayCard {

    public BrownCard() {

    }

    public BrownCard(Card card, Rank rank, Suit suit) {
        super(card, rank, suit);
    }

    @Override
    public String getColor() {
        return "brown";
    }

}