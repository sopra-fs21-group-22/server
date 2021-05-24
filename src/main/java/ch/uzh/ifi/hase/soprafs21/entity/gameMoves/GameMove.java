package ch.uzh.ifi.hase.soprafs21.entity.gameMoves;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedDate;

import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Entity
public class GameMove {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Player usingPlayer;

    @OneToOne
    private Player targetPlayer;

    @OneToOne
    private PlayCard card;

    @Column
    private GameMoveAction action;

    @Column
    private String message;

    @CreatedDate
    @Column
    private Date createdDate;

    public GameMove() {
    }

    public GameMove(Player usingPlayer, Player targetPlayer, PlayCard card, GameMoveAction action, String message) {
        this.usingPlayer = usingPlayer;
        this.targetPlayer = targetPlayer;
        this.card = card;
        this.action = action;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getUsingPlayer() {
        return usingPlayer;
    }

    public void setUsingPlayer(Player usingPlayer) {
        this.usingPlayer = usingPlayer;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public PlayCard getCard() {
        return card;
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

    public void setMessage(String message) {
        this.message = message;
    }

}
