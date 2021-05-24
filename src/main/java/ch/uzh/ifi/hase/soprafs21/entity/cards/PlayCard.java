package ch.uzh.ifi.hase.soprafs21.entity.cards;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.gameMoves.GameMove;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class PlayCard {

    @Id
    @GeneratedValue
    protected Long id;

    @Column
    protected Suit suit;

    @Column
    protected Rank rank;

    @Column
    protected String color;

    @Column
    protected Card card;

    @Column
    protected Priority priority;

    /**
     * Meant to be run only once when the card is on the users hand and played.
     * BlueCards will get added to the target player field cards.
     *
     * @param usingPlayer
     * @param targets
     */
    public void use(Player usingPlayer, Player target, PayLoadDTO payload) {
        if (target.getBullets() <= 0) {
            throw new GameLogicException("Target Player is already dead. Please don't attack corpses!");
        }

        if (!targetIsValid(usingPlayer, target)) {
            throw new GameLogicException(
                    String.format("Target player is not valid for card %s.", this.card.toString()));
        }

        String usingPlayerUsername = usingPlayer.getUser().getUsername();
        String targetUsername = target.getUser().getUsername();
        targetUsername = targetUsername.equals(usingPlayerUsername) ? "" : " on " + targetUsername;
        String message = String.format("%s uses card %s%s!", usingPlayerUsername, card.toString(), targetUsername);
        GameMove move = new GameMove(usingPlayer, target, this, GameMoveAction.USE, message);
        usingPlayer.getTable().addGameMove(move);
    }

    /**
     * Runs when the player having this card gets hit by a bang card
     *
     * @param affectedPlayer
     * @return True if the hit has been absorbed false otherwise
     */
    public boolean onBang(Player affectedPlayer) {
        return false;
    }

    /**
     * Validates if the target is a valid target. The card will end up on the field
     * of the target player.
     *
     * @param usingPlayer
     * @param targetPlayer
     */
    protected abstract boolean targetIsValid(Player usingPlayer, Player targetPlayer);

    /**
     * Responsible for the effect that occurs when the card is placed. Runs when
     * card is played from the players hand.
     *
     * @param usingPlayer
     * @param targetPlayer
     * @param payload
     */
    protected abstract void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload);

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return this.card.toString();
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
