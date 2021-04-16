package ch.uzh.ifi.hase.soprafs21.rest.dto.game;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

public class HandGetDTO {
    protected List<PlayCard> playCards;
    protected int cardsInHand;

    public List<PlayCardGetDTO> getPlayCards() {
        return null;
    }

    public void setPlayCards(List<PlayCard> playCards) {
        this.playCards = playCards;
    }

    public int getCardsInHand() {
        return this.playCards.size();
    }
}
