package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.gameMoves.GameMove;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

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
    protected void onPlacement(Player usingPlayer, Player targetPlayer, PayLoadDTO payload) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onBang(Player affectedPlayer) {

        PlayCard card = affectedPlayer.getTable().getDeck().drawCards(1).get(0);
        affectedPlayer.getTable().getDiscardPile().addCard(card);
        Boolean absorbsHit = card.getSuit().equals(Suit.HEARTS);

        String successMessage = String.format("BARREL %s to absorb hit on %s!", absorbsHit ? "succeeds" : "fails",
                affectedPlayer.getUser().getUsername());
        GameMove successGameMove = new GameMove(affectedPlayer, null, this,
                absorbsHit ? GameMoveAction.SUCCESS : GameMoveAction.FAIL, successMessage);
        affectedPlayer.getTable().addGameMove(successGameMove);

        return absorbsHit;
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}
