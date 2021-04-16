package ch.uzh.ifi.hase.soprafs21.rest.dto.game;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class HandGetDTO {
    protected List<PlayCard> playCards;

    public List<PlayCardGetDTO> getPlayCards() {
        if (playCards == null) {
            return new ArrayList<>();
        }
        List<PlayCardGetDTO> playCardsDTOs = new ArrayList<>();
        for (PlayCard card : playCards) {
            playCardsDTOs.add(DTOMapper.INSTANCE.convertEntityToPlayCardGetDTO(card));
        }
        return playCardsDTOs;
    }

    public void setPlayCards(List<PlayCard> playCards) {
        this.playCards = playCards;
    }
}
