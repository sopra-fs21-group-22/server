package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.VisibleCards;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.*;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotOnTurnException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.VisibleCardsGetDTO;
import ch.uzh.ifi.hase.soprafs21.service.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerGetAuthDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerTableGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.ReadyPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

@RestController
@RequestMapping("api/v1/games/")
public class GameController {

    @Autowired
    private HandService handService;

    @Autowired
    private PlayerTableService playerTableService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private PlayerTableRepository playerTableRepository;

    @Autowired
    private VisibleCardsService visibleCardsService;

    @PutMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerTableGetDTO joinGame(@RequestHeader("Authorization") String auth) {
        PlayerTable playerTable = playerTableService.addPlayer(userService.getIdByToken(auth));
        return DTOMapper.INSTANCE.convertEntityToPlayerTableGetDTO(playerTable);
    }

    @GetMapping("/{game_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGameInformation() {
        // TODO
        return null;
    }

    @GetMapping("/{game_id}/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerTableGetDTO getPlayerInformation(@RequestHeader("Authorization") String auth,
            @PathVariable Long game_id) {
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        return DTOMapper.INSTANCE.convertEntityToPlayerTableGetDTO(table);
    }

    @GetMapping("/{game_id}/players/{player_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerGetDTO getPlayerInformation(@PathVariable Long game_id, @PathVariable Long player_id,
            @RequestHeader("Authorization") String auth) {
        if (userService.idAndTokenMatch(player_id, auth.substring(7))) {
            return DTOMapper.INSTANCE.convertEntityToPlayerGetAuthDTO(playerRepository.getOne(player_id));
        }
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(playerRepository.getOne(player_id));

    }

    @PutMapping("/{game_id}/players/{player_id}/ready")
    @ResponseStatus(HttpStatus.OK)
    public void markPlayerAsReady(@PathVariable Long game_id, @PathVariable Long player_id,
            @RequestHeader("Authorization") String auth, @RequestBody ReadyPutDTO ready) {
        userService.throwIfNotIdAndTokenMatch(player_id, auth);
        playerTableService.setPlayerAsReady(game_id, player_id, ready.getStatus());
    }

    @GetMapping("/{game_id}/players/{player_id}/targets")
    @ResponseStatus(HttpStatus.OK)
    public List<PlayerGetDTO> getPlayersInRange(@PathVariable Long player_id, @PathVariable Long game_id) {
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        List<Player> players = table.getPlayersInRangeOf(player_id);
        List<PlayerGetDTO> playerGetDTOs = new ArrayList<>();
        for (Player player : players) {
            playerGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player));
        }

        return playerGetDTOs;
    }

    @PutMapping("/{game_id}/players/{player_id}/turn")
    @ResponseStatus(HttpStatus.OK)
    public void playerEndsTurn(@RequestHeader("Authorization") String auth, @PathVariable Long game_id,
            @PathVariable Long player_id) {
        userService.throwIfNotIdAndTokenMatch(player_id, auth);
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        if (!player_id.equals(table.getPlayerOnTurn().getId())) {
            throw new NotOnTurnException();
        }
        playerTableService.nextPlayersTurn(table);
    }

    @PostMapping("/{game_id}/players/{player_id}/hand/{card_id}")
    @ResponseStatus(HttpStatus.OK)
    public void playCard(@PathVariable Long game_id, @PathVariable Long player_id, @PathVariable Long card_id,
            @RequestBody List<Long> targets) {
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        Player usingPlayer = table.getPlayerByPlayerID(player_id);
        if (!table.getPlayerOnTurn().getId().equals(usingPlayer.getId())) {
            throw new NotOnTurnException();
        }
        List<Player> targetPlayers = table.getPlayersById(targets);

        usingPlayer.playCard(card_id, targetPlayers);
        playerRepository.save(usingPlayer);
        playerTableRepository.saveAndFlush(table);
    }

    @DeleteMapping("/{game_id}/players/{player_id}/hand/{card_id}")
    @ResponseStatus(HttpStatus.OK)
    public void discardCard(@PathVariable Long game_id, @PathVariable Long player_id, @PathVariable Long card_id) {
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        Player usingPlayer = table.getPlayerByPlayerID(player_id);
        if (!table.getPlayerOnTurn().getId().equals(usingPlayer.getId())) {
            throw new NotOnTurnException();
        }

        usingPlayer.getHand().removeCardById(card_id);
        playerTableRepository.saveAndFlush(table);
    }

    @GetMapping("/{game_id}/visiblecards")
    @ResponseStatus(HttpStatus.OK)
    public VisibleCardsGetDTO getVisibleCards(@PathVariable Long game_id) {
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        VisibleCards visibleCards = table.getVisibleCards();

        return DTOMapper.INSTANCE.convertEntityToVisibleCardsGetDTO(visibleCards);
    }

    @PostMapping("/{game_id}/visiblecards")
    @ResponseStatus(HttpStatus.OK)
    public void pickACard(@PathVariable Long game_id, @RequestBody List<Long> playersAndCards) {
        // playersAndCards = [player_id, card_id, player_id, card_id, etc.]
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        int number_of_players = table.getPlayers().size();
        VisibleCards visibleCards = table.getVisibleCards();

        // for each player: tell the visible cards which card to remove and the player
        // which card to add to his*her hand card
        for (int i = 0; i < number_of_players; i = i + 2) {
            Player currPlayer = table.getPlayerByPlayerID(playersAndCards.get(i));
            PlayCard card = visibleCards.getCardByID(playersAndCards.get(i + 1));
            currPlayer.pickACard(card);
            visibleCards.removeACard(card);
        }
        playerTableRepository.saveAndFlush(table);
    }
}