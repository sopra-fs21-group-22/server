package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class PlayerTable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "playertable_id")
    private List<Player> players;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "playertable_id")
    private Player playerOnTurn;

    @Column
    private Boolean gameHasStarted = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getPlayerOnTurn() {
        return playerOnTurn;
    }

    public void setPlayerOnTurn(Player playerOnTurn) {
        this.playerOnTurn = playerOnTurn;
    }

    public Boolean getGameHasStarted() {
        return gameHasStarted;
    }

    public void setGameHasStarted(Boolean gameHasStarted) {
        this.gameHasStarted = gameHasStarted;
    }

}