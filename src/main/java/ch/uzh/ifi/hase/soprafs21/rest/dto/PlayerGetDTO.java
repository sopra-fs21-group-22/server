package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class PlayerGetDTO {
    protected Integer range;
    protected Integer bullets;
    protected Long id;
    protected Integer rangeIncrement;
    protected User user;
    protected GameRole gameRole;

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

    public Integer getRangeIncrement() {
        return rangeIncrement;
    }

    public void setRangeIncrement(Integer rangeIncrement) {
        this.rangeIncrement = rangeIncrement;
    }

    public UserGetDTO getUser() {
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameRole getGameRole() {
        if (gameRole.equals(GameRole.SHERIFF) || bullets.equals(0)) {
            return gameRole;
        }
        return null;

    }

    public void setGameRole(GameRole gameRole) {
        this.gameRole = gameRole;
    }
}
