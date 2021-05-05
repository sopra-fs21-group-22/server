package ch.uzh.ifi.hase.soprafs21.controller.gameStateControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.exceptions.IllegalGameStateException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotOnTurnException;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.service.PlayerTableService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

@RestController
@RequestMapping("api/v1/games/")
public class OngoingController {

    @Autowired
    private PlayerTableService playerTableService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerTableRepository playerTableRepository;

    @PutMapping("/{game_id}/players/{player_id}/turn")
    @ResponseStatus(HttpStatus.OK)
    public void playerEndsTurn(@RequestHeader("Authorization") String auth, @PathVariable Long game_id,
            @PathVariable Long player_id) {
        playerTableService.checkGameState(game_id, GameStatus.ONGOING);
        userService.throwIfNotIdAndTokenMatch(player_id, auth);
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        if (!player_id.equals(table.getPlayerOnTurn().getId())) {
            throw new NotOnTurnException();
        }
        playerTableService.nextPlayersTurn(table);
        playerTableRepository.save(table);
    }
}
