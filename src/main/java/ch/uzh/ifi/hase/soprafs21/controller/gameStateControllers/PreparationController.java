package ch.uzh.ifi.hase.soprafs21.controller.gameStateControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.VisibleCards;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerTableGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.CharacterCardGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.ReadyPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.VisibleCardsGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.PlayerTableService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

@RestController
@RequestMapping("api/v1/games/")
public class PreparationController {

    @Autowired
    private PlayerTableService playerTableService;

    @Autowired
    private UserService userService;

    @Autowired
    private PlayerTableRepository playerTableRepository;

    @PutMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PlayerTableGetDTO joinGame(@RequestHeader("Authorization") String auth) {
        PlayerTable playerTable = playerTableService.addPlayer(userService.getIdByToken(auth));
        return DTOMapper.INSTANCE.convertEntityToPlayerTableGetDTO(playerTable);
    }

    @PutMapping("/{game_id}/players/{player_id}/ready")
    @ResponseStatus(HttpStatus.OK)
    public void markPlayerAsReady(@PathVariable Long game_id, @PathVariable Long player_id,
            @RequestHeader("Authorization") String auth, @RequestBody ReadyPutDTO ready) {
        playerTableService.checkGameState(game_id, GameStatus.PREPARATION);
        userService.throwIfNotIdAndTokenMatch(player_id, auth);
        Player player = userService.getUserById(player_id).getPlayer();
        playerTableService.setPlayerAsReady(game_id, player.getId(), ready.getStatus());
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

    @GetMapping("/{game_id}/players/{player_id}/characters")
    @ResponseStatus(HttpStatus.OK)
    public CharacterCardGetDTO receiveACharacter(@PathVariable Long game_id, @PathVariable Long player_id) {
        playerTableService.checkGameState(game_id, GameStatus.PREPARATION);
        Player player = playerTableService.getPlayerById(player_id);
        CharacterCard characterCard = player.getCharacterCard();

        return DTOMapper.INSTANCE.convertEntityToCharacterCardGetDTO(characterCard);
    }

}