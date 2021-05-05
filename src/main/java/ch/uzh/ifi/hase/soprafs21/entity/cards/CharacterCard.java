package ch.uzh.ifi.hase.soprafs21.entity.cards;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CharacterCard {
    @Id
    @GeneratedValue
    protected Long id;

    @Column
    protected String name;

    @Column
    protected Integer lifeAmount;

  

    /* public CharacterCard(String name, Integer lifeAmount){ //this causes a major bug
        this.name=name;
        this.lifeAmount=lifeAmount;
    } */

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

}
