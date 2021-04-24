package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PlayCardAuthGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PlayCardGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class DiscardPileGetDTO {
    private List<PlayCard> playCards;

    // public List<PlayCard> getPlayCards() {
    // return playCards;
    // }

    public void setPlayCards(List<PlayCard> playCards) {
        this.playCards = playCards;
    }

    public PlayCardAuthGetDTO getTopCard() {
        if (playCards.isEmpty()) {
            return null;
        }
        return DTOMapper.INSTANCE.convertEntityToPlayCardGetAuthDTO(this.playCards.get(0));
    }
}
