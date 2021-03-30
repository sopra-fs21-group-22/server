package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;

public class GamePutDTO {
    private PlayerTable playerTable;

    public PlayerTable getPlayerTable() {
        return playerTable;
    }

    public void setPlayerTable(PlayerTable playerTable) {
        this.playerTable = playerTable;
    }
}
