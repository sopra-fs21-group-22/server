package ch.uzh.ifi.hase.soprafs21.rest.dto.game;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;

public class CharacterCardGetDTO {

    protected Long id;
    protected String name;
    protected Integer lifeAmount;
    protected String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLifeAmount() {
        return lifeAmount;
    }

    public void setLifeAmount(Integer lifeAmount) {
        this.lifeAmount = lifeAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
