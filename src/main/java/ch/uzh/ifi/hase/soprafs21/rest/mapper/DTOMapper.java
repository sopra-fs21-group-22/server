package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.rest.dto.DiscardPileGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerGetAuthDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerTableGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserAuthGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserAuthPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserPutDTO;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper This class is responsible for generating classes that will
 * automatically transform/map the internal representation of an entity (e.g.,
 * the User) to the external/API representation (e.g., UserGetDTO for getting,
 * UserPostDTO for creating) and vice versa. Additional mappers can be defined
 * for new entities. Always created one mapper for getting information (GET) and
 * one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    //
    // User mappings
    //

    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "birthday", target = "birthday")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "status", target = "status")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "creationDate", target = "creationDate")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "jwt", target = "jwt")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "birthday", target = "birthday")
    UserAuthGetDTO convertEntityToUserAuthGetDTO(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserAuthPostDTOtoEntity(UserAuthPostDTO userAuthPostDTO);

    //
    // Game mappings
    //

    @Mapping(source = "id", target = "id")
    @Mapping(source = "playerOnTurn", target = "playerOnTurn")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "gameStatus", target = "gameStatus")
    PlayerTableGetDTO convertEntityToPlayerTableGetDTO(PlayerTable playerTable);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "gameRole", target = "gameRole")
    @Mapping(source = "ready", target = "ready")
    @Mapping(source = "leftNeighbor", target = "leftNeighbor")
    @Mapping(source = "rightNeighbor", target = "rightNeighbor")
    @Mapping(source = "hand", target = "hand")
    @Mapping(source = "onFieldCards", target = "onFieldCards")
    PlayerGetDTO convertEntityToPlayerGetDTO(Player player);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "gameRole", target = "gameRole")
    @Mapping(source = "ready", target = "ready")
    @Mapping(source = "leftNeighbor", target = "leftNeighbor")
    @Mapping(source = "rightNeighbor", target = "rightNeighbor")
    @Mapping(source = "hand", target = "hand")
    @Mapping(source = "onFieldCards", target = "onFieldCards")
    PlayerGetAuthDTO convertEntityToPlayerGetAuthDTO(Player player);

    @Mapping(source = "playCards", target = "playCards")
    HandGetDTO convertEntityToHandGetDTO(Hand hand);

    @Mapping(source = "playCards", target = "playCards")
    HandGetAuthDTO convertEntityToHandGetAuthDTO(Hand hand);

    @Mapping(source = "visibleCards", target = "visibleCards")
    VisibleCardsGetDTO convertEntityToVisibleCardsGetDTO(VisibleCards visibleCards);

    @Mapping(source = "onFieldCards", target = "onFieldCards")
    OnFieldCardsGetDTO convertEntityToOnFieldCardsGetDTO(OnFieldCards onFieldCards);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "suit", target = "suit")
    @Mapping(source = "rank", target = "rank")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "card", target = "card")
    PlayCardGetDTO convertEntityToPlayCardGetDTO(PlayCard playCard);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "suit", target = "suit")
    @Mapping(source = "rank", target = "rank")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "card", target = "card")
    PlayCardAuthGetDTO convertEntityToPlayCardGetAuthDTO(PlayCard playCard);

    @Mapping(source = "playCards", target = "playCards")
    DiscardPileGetDTO convertEntityTDiscardPileGetDTO(Deck discardPile);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lifeAmount", target = "lifeAmount")
    CharacterCardGetDTO convertEntityToCharacterCardGetDTO(CharacterCard characterCard);

}
