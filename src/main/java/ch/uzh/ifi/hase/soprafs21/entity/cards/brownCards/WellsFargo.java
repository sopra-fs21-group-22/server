package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

public class WellsFargo extends BrownCard {
    public WellsFargo() {
    }

    public WellsFargo(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.WELLSFARGO;
    }

    @Override
    public void use(Player usingPlayer, List<Player> targets) {
        super.use(usingPlayer, targets);
        Hand userHand = usingPlayer.getHand();
        List<PlayCard> cards = usingPlayer.getTable().getDeck().drawCards(3);
        userHand.addCards(cards);
    }

}
