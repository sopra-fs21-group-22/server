package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.gameMoves.GameMove;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

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
    public void onTurnStart(Player affectedPlayer) {
        PlayerTable table = affectedPlayer.getTable();
        Deck deck = table.getDeck();
        PlayCard referenceCard = deck.drawCards(1).get(0);
        table.getDiscardPile().addCard(referenceCard);
        if (referenceCard.getSuit() != Suit.HEARTS) {
            String failMessage = String.format("%s has to skip their move due to the JAIL card!",
                    affectedPlayer.getUser().getUsername());
            GameMove failGameMove = new GameMove(affectedPlayer, null, this, GameMoveAction.FAIL, failMessage);
            affectedPlayer.getTable().addGameMove(failGameMove);

            String outMessage = String.format("%s has left the JAIL!", affectedPlayer.getUser().getUsername());
            GameMove outGameMove = new GameMove(affectedPlayer, null, this, GameMoveAction.ACTIVATE, outMessage);
            affectedPlayer.getTable().addGameMove(outGameMove);

            table.setPlayerOnTurn(affectedPlayer.getAliveRightNeighbor());
        } else {
            String succMessage = String.format("%s managed to break out of JAIL!",
                    affectedPlayer.getUser().getUsername());
            GameMove successGameMove = new GameMove(affectedPlayer, null, this, GameMoveAction.SUCCESS, succMessage);
            affectedPlayer.getTable().addGameMove(successGameMove);
        }
        affectedPlayer.getOnFieldCards().removeOnFieldCard(this);
        table.getDiscardPile().addCard(this);
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

    @Override
    protected void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload) {
        // does nothing
    }
}
