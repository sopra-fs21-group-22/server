package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.HandGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.OnFieldCardsGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class PlayerGetDTO {
    protected Integer bullets;
    protected Long id;
    protected User user;
    protected GameRole gameRole;
    protected boolean ready;
    protected Player leftNeighbor;
    protected Player rightNeighbor;
    protected Hand hand;
    protected OnFieldCards onFieldCards;
    protected PlayerTable table;
    protected Integer strikes;
    protected Integer stillPlayableBangsThisRound;
    protected Integer maxBullets;

    public Integer getstillPlayableBangsThisRound() {
        return stillPlayableBangsThisRound;
    }

    public void setstillPlayableBangsThisRound(Integer stillPlayableBangsThisRound) {
        this.stillPlayableBangsThisRound = stillPlayableBangsThisRound;
    }

    public Long getTable() {
        return table.getId();
    }

    public void setTable(PlayerTable table) {
        this.table = table;
    }

    public HandGetDTO getHand() {
        return DTOMapper.INSTANCE.convertEntityToHandGetDTO(this.hand);
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public OnFieldCardsGetDTO getOnFieldCards() {
        return DTOMapper.INSTANCE.convertEntityToOnFieldCardsGetDTO(onFieldCards);
    }

    public void setOnFieldCards(OnFieldCards onFieldCards) {
        this.onFieldCards = onFieldCards;
    }

    public Integer getBullets() {
        return bullets;
    }

    public void setBullets(Integer bullets) {
        this.bullets = bullets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        if (user == null) {
            return "<User left Game>";
        }
        return user.getUsername();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameRole getGameRole() {
        if (table.getGameStatus() == GameStatus.ENDED || gameRole.equals(GameRole.SHERIFF) || bullets.equals(0)) {
            return gameRole;
        }
        return GameRole.HIDDEN;
    }

    public void setGameRole(GameRole gameRole) {
        this.gameRole = gameRole;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Long getLeftNeighbor() {
        if (this.leftNeighbor != null) {
            return leftNeighbor.getId();
        }
        return null;
    }

    public void setLeftNeighbor(Player leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    public Long getRightNeighbor() {
        if (this.rightNeighbor != null) {
            return rightNeighbor.getId();
        }
        return null;
    }

    public void setRightNeighbor(Player rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }

    public Integer getStrikes() {
        return strikes;
    }

    public void setStrikes(Integer strikes) {
        this.strikes = strikes;
    }

    public Integer getMaxBullets() {
        return maxBullets;
    }

    public void setMaxBullets(Integer maxBullets) {
        this.maxBullets = maxBullets;
    }

    /*
     * public Integer getCardamount() { return cardamount; }
     * 
     * public void setCardamount(Integer cardamount) { this.cardamount = cardamount;
     * }
     */
}
