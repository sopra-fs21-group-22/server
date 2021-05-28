package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class PrivPlayerTableGetDTO extends PlayerTableGetDTO {
    @Override
    public List<PlayerGetDTO> getPlayers() {
        List<PlayerGetDTO> playerGetDTOs = new ArrayList<>();
        for (Player player : players) {
            playerGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPlayerGetAuthDTO(player));
        }
        return playerGetDTOs;
    }
}
