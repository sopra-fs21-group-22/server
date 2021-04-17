package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.*;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.service.*;
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
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerTableGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.ReadyPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

@RestController
@RequestMapping("api/v1/games/")
public class GameController {

    @Autowired
    private HandService handService;

    @Autowired
    private SpecificCardService specificCardService;

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

    @GetMapping("/{game_id}/players/{player_id}/privates")
    @ResponseStatus(HttpStatus.OK)
    public PlayerGetAuthDTO getPrivateInformations(@RequestHeader("Authorization") String auth,
            @PathVariable Long game_id, @PathVariable Long player_id) {
        userService.throwIfNotIdAndTokenMatch(player_id, auth);
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        return DTOMapper.INSTANCE.convertEntityToPlayerGetAuthDTO(table.getPlayerById(player_id).get());
    }

    @PutMapping("/{game_id}/players/{player_id}/ready")
    @ResponseStatus(HttpStatus.OK)
    public void markPlayerAsReady(@PathVariable Long game_id, @PathVariable Long player_id,
            @RequestHeader("Authorization") String auth, @RequestBody ReadyPutDTO ready) {
        userService.throwIfNotIdAndTokenMatch(player_id, auth);
        playerTableService.setPlayerAsReady(game_id, player_id, ready.getStatus());
    }

    @PostMapping("/{game_id}/players/{player_id}/hand/{card_id}")
    @ResponseStatus(HttpStatus.OK)
    public void playCard(@PathVariable Long game_id, @PathVariable Long player_id, @PathVariable Long card_id,
            @RequestBody List<PlayerGetDTO> targetPlayerDTOs, @RequestHeader("Authorization") String auth) {

        // get player who is using card
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        Optional<Player> opt = table.getPlayerById(player_id);
        if (!opt.isPresent()) {
            throw new GameLogicException(
                    String.format("The using player with id %s is not in game with id %s", player_id, game_id));
        }
        Player usingPlayer = opt.get();

        // get target players card is played against
        List<Player> targetPlayers = new ArrayList<>();
        for (PlayerGetDTO targetPlayerGetDTO : targetPlayerDTOs) {
            Optional<Player> targetPlayerOpt = table.getPlayerById(targetPlayerGetDTO.getId());
            if (!targetPlayerOpt.isPresent()) {
                throw new GameLogicException(
                        "One or more target players are not in the same game as the using player.");
            }
            targetPlayers.add(targetPlayerOpt.get());
        }

        if (!table.getPlayerOnTurn().getId().equals(player_id)) {
            throw new GameLogicException("Player is not on turn!");
        }

        handService.layCard(table, usingPlayer, targetPlayers, card_id);
        playerTableRepository.save(table);
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
    public void playerEndsTurn(@RequestHeader("Authorization") String auth, @PathVariable Long game_id) {
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        Player currPlayer = table.getPlayerOnTurn();
        userService.throwIfNotIdAndTokenMatch(currPlayer.getId(), auth);
        playerTableService.nextPlayersTurn(table);
    }
}
