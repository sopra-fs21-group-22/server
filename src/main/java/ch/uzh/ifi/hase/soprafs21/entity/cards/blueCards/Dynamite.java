package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.gameMoves.GameMove;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public class Dynamite extends BlueCard {
    public Dynamite() {
    }

    public Dynamite(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.DYNAMITE;
        this.priority = Priority.FIRST;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player randomPlayer, PayLoadDTO payload) {
        // nothing happens
    }

    @Override
    public void onTurnStart(Player affectedPlayer) {
        PlayerTable table = affectedPlayer.getTable();
        PlayCard referenceCard = table.getDeck().drawCards(1).get(0);
        table.getDiscardPile().addCard(referenceCard);
        boolean correctNumberRange = false;
        for (int i = 1; i < 9; i++) {
            if (referenceCard.getRank() == Rank.values()[i]) {
                correctNumberRange = true;
            }
        }

        boolean dynamiteExplodes = correctNumberRange && (referenceCard.getSuit() == Suit.SPADES);

        if (dynamiteExplodes) {
            String explosionMessage = String.format("DYNAMITE explodes and deals 3 damage to %s!",
                    affectedPlayer.getUser().getUsername());
            GameMove explosionGameMove = new GameMove(affectedPlayer, null, this, GameMoveAction.SUCCESS,
                    explosionMessage);
            affectedPlayer.getTable().addGameMove(explosionGameMove);
            table.getDiscardPile().addCard(this);

            int lives = affectedPlayer.getBullets();
            int newLives = Math.max(lives - 3, 0);
            affectedPlayer.setBullets(newLives);
            /*
             * if (affectedPlayer.getCharacterCard().getName().equals("Bart Cassidy")){ //
             * Bart Cassidy Ability DeckService deckservice = new DeckService();
             * deckservice.cassidyDraw(affectedPlayer); }
             */
        } else {
            String explosionMessage = String.format("DYNAMITE doesn't explode and moves to the next player!");
            GameMove explosionGameMove = new GameMove(affectedPlayer, null, this, GameMoveAction.FAIL,
                    explosionMessage);
            affectedPlayer.getTable().addGameMove(explosionGameMove);

            Player leftNeighbor = affectedPlayer.getLeftNeighbor();
            while (leftNeighbor.getBullets() == 0) {
                leftNeighbor = leftNeighbor.getLeftNeighbor();
            }
            leftNeighbor.getOnFieldCards().addOnFieldCard(this);
        }
        affectedPlayer.getOnFieldCards().removeOnFieldCard(this);
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}
