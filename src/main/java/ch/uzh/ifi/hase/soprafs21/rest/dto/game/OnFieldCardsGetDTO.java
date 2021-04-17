package ch.uzh.ifi.hase.soprafs21.rest.dto.game;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class OnFieldCardsGetDTO {
    private List<BlueCard> onFieldCards;

    public List<PlayCardAuthGetDTO> getOnFieldCards() {
        List<PlayCardAuthGetDTO> cards = new ArrayList<>();
        for (BlueCard card : onFieldCards) {
            cards.add(DTOMapper.INSTANCE.convertEntityToPlayCardGetAuthDTO(card));
        }
        return cards;
    }

    public void setOnFieldCards(List<BlueCard> onFieldCards) {
        this.onFieldCards = onFieldCards;
    }
}
