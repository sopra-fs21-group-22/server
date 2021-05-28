package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

public abstract class Horse extends BlueCard {
    @Override
    public void use(Player usingPlayer, Player target, PayLoadDTO payload) {
        List<Card> horses = new ArrayList<>();
        horses.add(Card.APPALOOSA);
        horses.add(Card.MUSTANG);
        List<BlueCard> fieldCards = usingPlayer.getOnFieldCards().getOnFieldCards();
        for (BlueCard card : fieldCards) {
            if (card.getCard() == this.getCard()) {
                throw new GameLogicException("A card with the same name is already on the field!");
            }
            if (horses.contains(card.getCard())) {
                fieldCards.remove(card);
                usingPlayer.getTable().getDiscardPile().addCard(card);
                break;
            }
        }
        super.use(usingPlayer, target, payload);
    }
}
