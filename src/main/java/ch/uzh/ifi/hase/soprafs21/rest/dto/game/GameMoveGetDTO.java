package ch.uzh.ifi.hase.soprafs21.rest.dto.game;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

public class GameMoveGetDTO {

    private Long id;

    private Player usingPlayer;

    private Player targetPlayer;

    private PlayCard card;

    private GameMoveAction action;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUsingPlayer() {
        if (usingPlayer == null) {
            return null;
        }
        return usingPlayer.getId();
    }

    public void setUsingPlayer(Player usingPlayer) {
        this.usingPlayer = usingPlayer;
    }

    public Long getTargetPlayer() {
        if (targetPlayer == null) {
            return null;
        }
        return targetPlayer.getId();
    }

    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public String getCard() {
        if (card == null) {
            return null;
        }
        return card.getCard().toString();
    }

    public void setCard(PlayCard card) {
        this.card = card;
    }

    public GameMoveAction getAction() {
        return action;
    }

    public void setAction(GameMoveAction action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }
}
