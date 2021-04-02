package ch.uzh.ifi.hase.soprafs21.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerGetAuthDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerTableGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.PlayerTableService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

@RestController
@RequestMapping("api/v1/games/")
public class GameController {

    @Autowired
    private PlayerTableService playerTableService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserService userService;

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
            @RequestHeader String auth) {
        if (userService.idAndTokenMatch(player_id, auth.substring(7))) {
            return DTOMapper.INSTANCE.convertEntityToPlayerGetAuthDTO(playerRepository.getOne(player_id));
        }
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(playerRepository.getOne(player_id));

    }

    @PutMapping("/{game_id}/players/{player_id}/start")
    @ResponseStatus(HttpStatus.OK)
    public void startGame(@PathVariable Long game_id, @PathVariable Long player_id,
            @RequestHeader("Authorization") String auth) {
        // check if user trying to start his own game
        userService.throwIfNotIdAndTokenMatch(player_id, auth);
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        for (Player player : table.getPlayers()) {
            if (player.getId().equals(player_id)) {
                playerTableService.startGame(game_id);
                return;
            }
        }

        throw new IllegalArgumentException("Player is not in game they are trying to start.");
    }
}
