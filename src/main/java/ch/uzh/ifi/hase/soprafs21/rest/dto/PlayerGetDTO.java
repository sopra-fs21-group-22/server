package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class PlayerGetDTO {
    protected Integer range;
    protected Integer bullets;
    protected Long id;
    protected User user;
    protected GameRole gameRole;
    protected boolean ready;
    protected int distanceIncreaseForOthers;
    protected int distanceDecreaseToOthers;
    protected Player leftNeighbor;
    protected Player rightNeighbor;

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
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
        return user.getUsername();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameRole getGameRole() {
        if (gameRole.equals(GameRole.SHERIFF) || bullets.equals(0)) {
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

    public int getDistanceIncreaseForOthers() {
        return distanceIncreaseForOthers;
    }

    public void setDistanceIncreaseForOthers(int distanceIncreaseForOthers) {
        this.distanceIncreaseForOthers = distanceIncreaseForOthers;
    }

    public int getDistanceDecreaseToOthers() {
        return distanceDecreaseToOthers;
    }

    public void setDistanceDecreaseToOthers(int distanceDecreaseToOthers) {
        this.distanceDecreaseToOthers = distanceDecreaseToOthers;
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
}
