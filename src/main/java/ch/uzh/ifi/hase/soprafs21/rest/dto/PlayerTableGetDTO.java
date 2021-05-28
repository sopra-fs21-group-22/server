package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.gameMoves.GameMove;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.GameMoveGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class PlayerTableGetDTO {
    protected Long id;
    protected GameStatus gameStatus;
    protected Deck discardPile;
    protected Player playerOnTurn;
    protected List<Player> players;
    protected Long timeRemaining;
    protected Chat chat;
    protected List<GameMove> gameMoves;

    public List<GameMoveGetDTO> getGameMoves() {
        List<GameMoveGetDTO> dtos = new ArrayList<>();
        for (GameMove gameMove : gameMoves) {
            dtos.add(DTOMapper.INSTANCE.convertEntityToGameMoveGetDTO(gameMove));
        }
        return dtos;
    }

    public void setGameMoves(List<GameMove> gameMoves) {
        this.gameMoves = gameMoves;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

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

    public Long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(Long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

}
