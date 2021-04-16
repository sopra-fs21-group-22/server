package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.HandGetAuthDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.HandGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class PlayerGetAuthDTO extends PlayerGetDTO {

    @Override
    public GameRole getGameRole() {
        return gameRole;
    }

    @Override
    public HandGetAuthDTO getHand() {
        return DTOMapper.INSTANCE.convertEntityToHandGetAuthDTO(hand);
    }
}
