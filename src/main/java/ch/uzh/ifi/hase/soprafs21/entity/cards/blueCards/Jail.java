package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import ch.uzh.ifi.hase.soprafs21.constant.*;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

import javax.persistence.Entity;

@Entity
public class Jail extends BlueCard {

    public Jail() {
    }

    public Jail(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.JAIL;
        this.priority = Priority.SECOND;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload) {
        if (target.getGameRole() != GameRole.SHERIFF) {
            target.getOnFieldCards().addOnFieldCard(this);
        } else {
            throw new GameLogicException("You can't put the Sheriff in Jail!");
        }
    }

    @Override
    public void onTurnStart(Player affectedPlayer) {
        PlayerTable table = affectedPlayer.getTable();
        Deck deck = table.getDeck();
        PlayCard referenceCard = deck.drawCards(1).get(0);
        if (referenceCard.getSuit() != Suit.HEARTS) {
            table.setPlayerOnTurn(affectedPlayer.getRightNeighbor());
        }
        affectedPlayer.getOnFieldCards().removeOnFieldCard(this); // card is removed whether or not the player stays in
                                                                  // jail for current turn
    }

    @Override
    public void onRemoval(Player affectedPlayer) {
        // does nothing
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        boolean targetIsUser = usingPlayer.getId().equals(targetPlayer.getId());
        boolean targetIsSheriff = targetPlayer.getGameRole().equals(GameRole.SHERIFF);
        return !targetIsSheriff && !targetIsUser;
    }
}
