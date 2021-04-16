package ch.uzh.ifi.hase.soprafs21.rest.dto.game;

import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;

public class OnFieldCardsGetDTO {
    private List<BlueCard> onFieldCards;

    public List<BlueCard> getOnFieldCards() {
        return onFieldCards;
    }

    public void setOnFieldCards(List<BlueCard> onFieldCards) {
        this.onFieldCards = onFieldCards;
    }
}
