package ch.uzh.ifi.hase.soprafs21.rest.dto.game;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

import java.util.ArrayList;
import java.util.List;

public class VisibleCardsGetDTO {
    private List<PlayCard> visibleCards;

    public List<PlayCardAuthGetDTO> getVisibleCards() {
        List<PlayCardAuthGetDTO> cards = new ArrayList<>();
        for (PlayCard card : visibleCards) {
            cards.add(DTOMapper.INSTANCE.convertEntityToPlayCardGetAuthDTO(card));
        }
        return cards;
    }

    public void setVisibleCards(List<PlayCard> visibleCards) {
        this.visibleCards = visibleCards;
    }



}
