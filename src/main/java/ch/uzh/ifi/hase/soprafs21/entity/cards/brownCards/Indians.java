package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public class Indians extends BrownCard {

    public Indians() {
    }

    public Indians(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.INDIANS;
        this.priority = Priority.ELEVENTH;

    }

    @Override
    protected void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload) {
        List<Player> players = usingPlayer.getTable().getAlivePlayers();
        for (int i = 0; i < players.size(); i++) {
            Player currPlayer = players.get(i);
            if (currPlayer.getId().equals(usingPlayer.getId())) {
                continue;
            }
            if (!removeBang(currPlayer)) {
                String succMessage = String.format("%s discarded a BANG card to avoid taking damage!",
                affectedPlayer.getUser().getUsername());
                GameMove succGameMove = new GameMove(affectedPlayer, null, this, GameMoveAction.SUCCESS, succMessage);
                affectedPlayer.getTable().addGameMove(succGameMove);

                currPlayer.takeUnblockableHit(usingPlayer);
            }else{
                String succMessage = String.format("%s couldn't avoid taking damage!",
                affectedPlayer.getUser().getUsername());
                GameMove succGameMove = new GameMove(affectedPlayer, null, this, GameMoveAction.FAIL, succMessage);
                affectedPlayer.getTable().addGameMove(succGameMove);
            }
        }
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }

    private boolean removeBang(Player player) {
        List<PlayCard> cards = player.getHand().getPlayCards();
        Deck discardPile = player.getTable().getDiscardPile();

        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getCard() == Card.BANG) {
                discardPile.addCard(cards.remove(i));
                return true;
            }
        }
        return false;
    }

}
