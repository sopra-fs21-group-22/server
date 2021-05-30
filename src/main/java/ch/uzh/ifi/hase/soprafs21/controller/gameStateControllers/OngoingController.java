package ch.uzh.ifi.hase.soprafs21.controller.gameStateControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.gameMoves.GameMove;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotOnTurnException;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.CharacterCardGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
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

    @Autowired
    private PlayerRepository playerRepository;

    @PutMapping("/{game_id}/players/{player_id}/turn")
    @ResponseStatus(HttpStatus.OK)
    public void playerEndsTurn(@RequestHeader("Authorization") String auth, @PathVariable Long game_id,
            @PathVariable Long player_id) {
        playerTableService.checkGameState(game_id, GameStatus.ONGOING);
        Player player = playerRepository.getOne(player_id);
        userService.throwIfNotIdAndTokenMatch(player.getUser().getId(), auth);
        PlayerTable table = playerTableService.getPlayerTableById(game_id);

        if (!player.getId().equals(table.getPlayerOnTurn().getId())) {
            throw new NotOnTurnException();
        }
        playerTableService.nextPlayersTurn(table);
        playerTableRepository.save(table);
    }

    @PostMapping("/{game_id}/players/{player_id}/hand/{card_id}/target/{target_id}")
    @ResponseStatus(HttpStatus.OK)
    public void playCardWithTarget(@RequestHeader("Authorization") String auth, @PathVariable Long game_id,
            @PathVariable Long player_id, @PathVariable Long card_id, @PathVariable Long target_id,
            @RequestBody(required = false) PayLoadDTO payload) {

        playerTableService.checkGameState(game_id, GameStatus.ONGOING);
        Player usingPlayer = playerRepository.getOne(player_id);
        userService.throwIfNotIdAndTokenMatch(usingPlayer.getUser().getId(), auth);
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        if (!table.getPlayerOnTurn().getId().equals(usingPlayer.getId())) {
            throw new NotOnTurnException();
        }
        Player targetPlayer = table.getPlayerByPlayerID(target_id);
        usingPlayer.playCard(card_id, targetPlayer, payload);
        playerRepository.save(usingPlayer);
        playerTableRepository.saveAndFlush(table);
    }

    @PostMapping("/{game_id}/players/{player_id}/hand/{card_id}")
    @ResponseStatus(HttpStatus.OK)
    public void playCardWithoutTarget(@RequestHeader("Authorization") String auth, @PathVariable Long game_id,
            @PathVariable Long player_id, @PathVariable Long card_id) {
        playerTableService.checkGameState(game_id, GameStatus.ONGOING);
        Player usingPlayer = playerRepository.getOne(player_id);
        userService.throwIfNotIdAndTokenMatch(usingPlayer.getUser().getId(), auth);
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        if (!table.getPlayerOnTurn().getId().equals(usingPlayer.getId())) {
            throw new NotOnTurnException();
        }
        usingPlayer.playCard(card_id, usingPlayer, null);
        playerRepository.save(usingPlayer);
        playerTableRepository.saveAndFlush(table);
    }

    @DeleteMapping("/{game_id}/players/{player_id}/hand/{card_id}")
    @ResponseStatus(HttpStatus.OK)
    public void discardCard(@RequestHeader("Authorization") String auth, @PathVariable Long game_id,
            @PathVariable Long player_id, @PathVariable Long card_id) {
        playerTableService.checkGameState(game_id, GameStatus.ONGOING);
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        Player usingPlayer = playerRepository.getOne(player_id);
        userService.throwIfNotIdAndTokenMatch(usingPlayer.getUser().getId(), auth);
        if (!table.getPlayerOnTurn().getId().equals(usingPlayer.getId())) {
            throw new NotOnTurnException();
        }

        PlayCard removedCard = usingPlayer.getHand().removeCardById(card_id);
        table.getDiscardPile().addCard(removedCard);

        String message = String.format("%s discarded card %s.", usingPlayer.getUser().getUsername(),
                removedCard.getCard());
        table.addGameMove(new GameMove(usingPlayer, null, removedCard, GameMoveAction.DISCARD, message));

        playerTableRepository.saveAndFlush(table);

    }

    @PutMapping("/{game_id}/players/{player_id}/chat")
    @ResponseStatus(HttpStatus.OK)
    public void writeInChat(@RequestHeader("Authorization") String auth, @PathVariable Long game_id,
            @PathVariable Long player_id, @RequestBody Message message) {
        
        PlayerTable table = playerTableService.getPlayerTableById(game_id);
        Player usingPlayer = playerRepository.getOne(player_id);
        userService.throwIfNotIdAndTokenMatch(usingPlayer.getUser().getId(), auth);
        playerTableService.addMessage(table, message.getContent(), message.getName());
        playerTableRepository.saveAndFlush(table);
    }

    @GetMapping("/{game_id}/players/{player_id}/characters")
    @ResponseStatus(HttpStatus.OK)
    public CharacterCardGetDTO receiveACharacter(@PathVariable Long game_id, @PathVariable Long player_id) {
        playerTableService.checkGameState(game_id, GameStatus.ONGOING);
        Player player = playerTableService.getPlayerById(player_id);
        CharacterCard characterCard = player.getCharacterCard();

        return DTOMapper.INSTANCE.convertEntityToCharacterCardGetDTO(characterCard);
    }
}
