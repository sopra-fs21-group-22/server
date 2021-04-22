package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Entity
public class StageCoach extends BrownCard {
    public StageCoach() {
    }

    public StageCoach(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.STAGECOACH;
    }

    @Override
    public void use(Player usingPlayer, List<Player> targets) {
        super.use(usingPlayer, targets);
        Hand userHand = usingPlayer.getHand();
        List<PlayCard> cards = usingPlayer.getTable().getDeck().drawCards(2);
        userHand.addCards(cards);
    }
}
