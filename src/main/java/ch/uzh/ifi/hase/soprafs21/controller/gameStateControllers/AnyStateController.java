package ch.uzh.ifi.hase.soprafs21.controller.gameStateControllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerTableGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.PlayerTableService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

@RestController
@RequestMapping("/api/v1/games")
public class AnyStateController {

    @Autowired
    private PlayerTableService playerTableService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    PlayerTableRepository playerTableRepository;

    @GetMapping("/{game_id}/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerTableGetDTO getPlayerInformation(@RequestHeader("Authorization") String auth,
            @PathVariable Long game_id) {
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        playerTableService.updateTimer(playerTableService.getPlayerTableById(game_id));
        return DTOMapper.INSTANCE.convertEntityToPlayerTableGetDTO(table);
    }

    @GetMapping("/{game_id}/players/{player_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerGetDTO getPlayerInformation(@PathVariable Long game_id, @PathVariable Long player_id,
            @RequestHeader("Authorization") String auth) {
        Player player = userService.getUserById(player_id).getPlayer();
        if (userService.idAndTokenMatch(player_id, auth.substring(7))) {
            return DTOMapper.INSTANCE.convertEntityToPlayerGetAuthDTO(playerRepository.getOne(player.getId()));
        }
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(playerRepository.getOne(player.getId()));

    }

    @GetMapping("/{game_id}/players/{player_id}/targets")
    @ResponseStatus(HttpStatus.OK)
    public List<PlayerGetDTO> getPlayersInRange(@PathVariable Long player_id, @PathVariable Long game_id) {
        Player currPlayer = userService.getUserById(player_id).getPlayer();
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        List<Player> players = table.getPlayersInRangeOf(currPlayer.getId());
        List<PlayerGetDTO> playerGetDTOs = new ArrayList<>();
        for (Player player : players) {
            playerGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player));
        }

        return playerGetDTOs;
    }

    @DeleteMapping("/{game_id}/players/{player_id}")
    @ResponseStatus(HttpStatus.OK)
    public void leaveGame(@RequestHeader("Authorization") String auth, @PathVariable Long game_id,
            @PathVariable Long player_id) {
        userService.throwIfNotIdAndTokenMatch(player_id, auth);
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        Player player = userService.getUserById(player_id).getPlayer();
        player.setUser(null);
        if (table.getGameStatus() != GameStatus.ENDED) {
            player.setBullets(0);
            player.onDeath();
            if (player.getTable().getPlayerOnTurn().getId().equals(player.getId())) {
                playerTableService.nextPlayersTurn(player.getTable());
            }
        }

        playerTableRepository.save(table);
    }
}
