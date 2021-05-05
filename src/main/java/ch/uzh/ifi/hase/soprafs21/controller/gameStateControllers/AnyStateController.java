package ch.uzh.ifi.hase.soprafs21.controller.gameStateControllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
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
}
