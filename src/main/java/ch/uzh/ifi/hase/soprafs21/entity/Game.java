package ch.uzh.ifi.hase.soprafs21.entity;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "playertable_id")
    private PlayerTable playerTable;

    public PlayerTable getPlayerTable() {
        return playerTable;
    }

    public void setPlayerTable(PlayerTable playerTable) {
        this.playerTable = playerTable;
    }

}
