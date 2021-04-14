package ch.uzh.ifi.hase.soprafs21.entity;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import java.util.ArrayList;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;

/**
 * The Player class represents a player The id is the same as the user who
 * "owns" the player has.
 */
@Entity
public class Player {

    @Id
    private Long id;

    @Column
    private Integer bullets = 4;

    @Column
    private Integer range = 1;

    @Column
    private Integer distanceIncreaseForOthers = 0;

    @Column
    Integer distanceDecreaseToOthers = 0;

    @OneToOne
    private User user;

    @OneToOne
    private Hand hand;

    @Column
    private GameRole gameRole = GameRole.HIDDEN;

    @Column
    private Integer tablePosition;

    @Column
    private Boolean ready = false;

    @Column
    private Integer cardamount;

    public void takeHit() {
        this.bullets -= 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand){
        this.hand = hand;
    }

    public GameRole getGameRole() {
        return gameRole;
    }

    public void setGameRole(GameRole gameRole) {
        this.gameRole = gameRole;
    }

    public Integer getTablePosition() {
        return tablePosition;
    }

    public void setTablePosition(Integer tablePosition) {
        this.tablePosition = tablePosition;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public Integer getCardamount() {
        return hand.getPlayCards().size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        // this causes a bug for some weird reason
        // if (obj.getClass() != this.getClass()) {
        // return false;
        // }

        final Player other = (Player) obj;

        return other.getId().equals(this.getId());
    }

    public Integer getDistanceIncreaseForOthers() {
        return distanceIncreaseForOthers;
    }

    public void setDistanceIncreaseForOthers(Integer distanceIncreaseForOthers) {
        this.distanceIncreaseForOthers = distanceIncreaseForOthers;
    }

    public Integer getDistanceDecreaseToOthers() {
        return distanceDecreaseToOthers;
    }

    public void setDistanceDecreaseToOthers(Integer distanceDecreaseToOthers) {
        this.distanceDecreaseToOthers = distanceDecreaseToOthers;
    }
}
