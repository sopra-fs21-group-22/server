package ch.uzh.ifi.hase.soprafs21.rest.dto.game;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class HandGetAuthDTO extends HandGetDTO {

    @Override
    public List<PlayCardGetDTO> getPlayCards() {
        if (playCards == null) {

            return new ArrayList<>();
        }
        List<PlayCardGetDTO> playCardsDTOs = new ArrayList<>();
        for (PlayCard card : playCards) {
            playCardsDTOs.add(DTOMapper.INSTANCE.convertEntityToPlayCardGetAuthDTO(card));
        }
        return playCardsDTOs;
    }

}
