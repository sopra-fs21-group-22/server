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

import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePutDTO;
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
    public List<PlayerTableGetDTO> getPlayerInformation() {
        // TODO
        return null;
    }
}
