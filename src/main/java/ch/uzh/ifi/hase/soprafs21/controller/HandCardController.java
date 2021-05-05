package ch.uzh.ifi.hase.soprafs21.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotOnTurnException;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;
import ch.uzh.ifi.hase.soprafs21.service.DeckService;
import ch.uzh.ifi.hase.soprafs21.service.HandService;
import ch.uzh.ifi.hase.soprafs21.service.PlayerTableService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import ch.uzh.ifi.hase.soprafs21.service.VisibleCardsService;

@RestController
@RequestMapping("api/v1/games/{game_id}/players/{player_id}/hand/")
public class HandCardController {
    @Autowired
    private HandService handService;

    @Autowired
    private PlayerTableService playerTableService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerTableRepository playerTableRepository;

    @PostMapping("/{card_id}/target/{target_id}")
    @ResponseStatus(HttpStatus.OK)
    public void playCard(@PathVariable Long game_id, @PathVariable Long player_id, @PathVariable Long card_id,
            @PathVariable Long target_id, @RequestBody(required = false) PayLoadDTO payload) {
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        Player usingPlayer = table.getPlayerByPlayerID(player_id);
        if (!table.getPlayerOnTurn().getId().equals(usingPlayer.getId())) {
            throw new NotOnTurnException();
        }
        Player targetPlayer = table.getPlayerByPlayerID(target_id);
        usingPlayer.playCard(card_id, targetPlayer, payload);
        playerRepository.save(usingPlayer);
        playerTableRepository.saveAndFlush(table);
    }

    @DeleteMapping("/{card_id}")
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
}
