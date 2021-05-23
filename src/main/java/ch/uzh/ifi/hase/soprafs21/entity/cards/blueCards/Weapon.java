package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public abstract class Weapon extends BlueCard {
    @Override
    public void use(Player usingPlayer, Player target, PayLoadDTO payload) {
        List<Card> weapons = new ArrayList<>();
        weapons.add(Card.REMINGTON);
        weapons.add(Card.WINCHESTER);
        weapons.add(Card.SCHOFIELD);
        weapons.add(Card.CARABINE);
        weapons.add(Card.VOLCANIC);
        PlayerTable table = usingPlayer.getTable();
        List<BlueCard> fieldCards = usingPlayer.getOnFieldCards().getOnFieldCards();
        for (BlueCard card : fieldCards) {
            if (card.getCard() == this.getCard()) {
                throw new GameLogicException("A card with the same name is already on the field!");
            }
            if (weapons.contains(card.getCard())) {
                fieldCards.remove(card);
                table.getDiscardPile().addCard(card);
                break;
            }
        }
        super.use(usingPlayer, target, payload);
    }
}
