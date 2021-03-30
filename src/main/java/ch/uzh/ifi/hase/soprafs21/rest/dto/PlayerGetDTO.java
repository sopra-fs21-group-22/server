package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class PlayerGetDTO {
    private Integer range;
    private Integer bullets;
    private Long id;
    private Integer rangeIncrement;
    private User user;

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
}
