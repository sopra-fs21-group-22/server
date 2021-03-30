package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerTableGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserAuthGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserAuthPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPutDTO;

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
    PlayerTableGetDTO convertEntityToPlayerTableGetDTO(PlayerTable playerTable);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "range", target = "range")
    @Mapping(source = "bullets", target = "bullets")
    @Mapping(source = "rangeIncrement", target = "rangeIncrement")
    @Mapping(source = "user", target = "user")
    PlayerGetDTO convertEntityToPlayerGetDTO(Player player);

}
