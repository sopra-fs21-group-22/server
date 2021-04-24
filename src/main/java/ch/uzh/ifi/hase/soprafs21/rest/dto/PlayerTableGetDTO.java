package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class PlayerTableGetDTO {
    private Player playerOnTurn;
    private List<Player> players;
    private Long id;
    private Deck discardPile;

    public DiscardPileGetDTO getDiscardPile() {
        return DTOMapper.INSTANCE.convertEntityTDiscardPileGetDTO(discardPile);
    }

    public void setDiscardPile(Deck discardPile) {
        this.discardPile = discardPile;
    }

    public PlayerGetDTO getPlayerOnTurn() {
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(playerOnTurn);
    }

    public void setPlayerOnTurn(Player playerOnTurn) {
        this.playerOnTurn = playerOnTurn;
    }

    public List<PlayerGetDTO> getPlayers() {
        List<PlayerGetDTO> playerGetDTOs = new ArrayList<PlayerGetDTO>();
        for (Player player : players) {
            playerGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player));
        }
        return playerGetDTOs;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
