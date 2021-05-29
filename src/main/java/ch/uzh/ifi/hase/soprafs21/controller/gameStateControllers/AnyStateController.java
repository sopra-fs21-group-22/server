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
        if (table.getGameStatus() == GameStatus.ONGOING) {
            playerTableService.updateTimer(table);
        }
        return DTOMapper.INSTANCE.convertEntityToPlayerTableGetDTO(table);
    }

    @GetMapping("/{game_id}/players/{player_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerGetDTO getPlayerInformation(@PathVariable Long game_id, @PathVariable Long player_id,
            @RequestHeader("Authorization") String auth) {
        Player player = playerRepository.getOne(player_id);
        if (userService.idAndTokenMatch(player.getUser().getId(), auth.substring(7))) {
            return DTOMapper.INSTANCE.convertEntityToPlayerGetAuthDTO(playerRepository.getOne(player.getId()));
        }
        return DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(playerRepository.getOne(player.getId()));

    }

    @GetMapping("/{game_id}/players/{player_id}/targets")
    @ResponseStatus(HttpStatus.OK)
    public List<PlayerGetDTO> getPlayersInRange(@PathVariable Long player_id, @PathVariable Long game_id) {
        Player currPlayer = playerRepository.getOne(player_id);
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
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        Player player = playerRepository.getOne(player_id);
        userService.throwIfNotIdAndTokenMatch(player.getUser().getId(), auth);
        if (table.getGameStatus() != GameStatus.PREPARATION) {
            player.setBullets(0);
            player.setUser(null);
        } else {
            playerRepository.delete(player);
        }
        if (table.getPlayers().size() > 0) {
            playerTableRepository.save(table);
        } else {
            playerTableRepository.delete(table);
        }

    }

}
