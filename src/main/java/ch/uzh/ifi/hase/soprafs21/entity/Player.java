package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;


import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;

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

    @Column
    Integer maxAmountOfBangPlayed = 1;

    @Column
    Integer stillPlayableBangsThisRound = 1;

    @OneToOne
    private User user;

    @Column
    private GameRole gameRole = GameRole.HIDDEN;

    @OneToOne
    private Player leftNeighbor;

    @OneToOne
    private Player rightNeighbor;

    @Column
    private Boolean ready = false;

    @OneToOne
    private OnFieldCards onFieldCards;

    public void takeHit() {
        this.bullets -= 1;

    public boolean reachesWithWeapon(Player targetPlayer) {
        Player userRightNeighbor = this.getRightNeighbor();
        Player userLeftNeighbor = this.getLeftNeighbor();
        int rightDistance = 1;
        int leftDistance = 1;
        for (int i = 0; i < 7; i++) {
            if (userRightNeighbor.getId().equals(targetPlayer.getId())) {
                break;
            }
            rightDistance++;
            userRightNeighbor = userRightNeighbor.getRightNeighbor();
        }
        for (int i = 0; i < 7; i++) {
            if (userLeftNeighbor.getId().equals(targetPlayer.getId())) {
                break;
            }
            leftDistance++;
            userLeftNeighbor = userLeftNeighbor.getLeftNeighbor();
        }
        int distance = rightDistance < leftDistance ? rightDistance : leftDistance;

        return distance - this.getRange() + this.getDistanceDecreaseToOthers()
                + targetPlayer.getDistanceIncreaseForOthers() <= 0;

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

    public GameRole getGameRole() {
        return gameRole;
    }

    public void setGameRole(GameRole gameRole) {
        this.gameRole = gameRole;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
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

    public OnFieldCards getOnFieldCards() { return onFieldCards; }

    public void setOnFieldCards(OnFieldCards cards) { this.onFieldCards = cards; }

    public Player getLeftNeighbor() {
        return leftNeighbor;
    }

    public void setLeftNeighbor(Player leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    public Player getRightNeighbor() {
        return rightNeighbor;
    }

    public void setRightNeighbor(Player rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }

    public Integer getMaxAmountOfBangPlayed() {
        return maxAmountOfBangPlayed;
    }

    public void setMaxAmountOfBangPlayed(Integer maxAmountOfBangPlayed) {
        this.maxAmountOfBangPlayed = maxAmountOfBangPlayed;
    }

    public Integer getStillPlayableBangsThisRound() {
        return stillPlayableBangsThisRound;
    }

    public void setStillPlayableBangsThisRound(Integer stillPlayableBangsThisRound) {
        this.stillPlayableBangsThisRound = stillPlayableBangsThisRound;
    }

}
