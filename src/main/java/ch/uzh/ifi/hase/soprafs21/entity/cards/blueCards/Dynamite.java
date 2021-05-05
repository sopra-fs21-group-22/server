package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;
import ch.uzh.ifi.hase.soprafs21.service.DeckService;


import javax.persistence.Entity;

@Entity
public class Dynamite extends BlueCard {
    public Dynamite() {
    }

    public Dynamite(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.DYNAMITE;
        this.priority = Priority.FIRST;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player randomPlayer, PayLoadDTO payload) {
        // nothing happens
    }

    @Override
    public void onTurnStart(Player affectedPlayer) {
        PlayerTable table = affectedPlayer.getTable();
        PlayCard referenceCard = table.getDeck().drawCards(1).get(0);
        boolean correctNumberRange = false;
        for (int i = 1; i < 9; i++) {
            if (referenceCard.getRank() == Rank.values()[i]) {
                correctNumberRange = true;
            }
        }

        boolean dynamiteExplodes = correctNumberRange && (referenceCard.getSuit() == Suit.SPADES);

        if (dynamiteExplodes) {
            int lives = affectedPlayer.getBullets();
            affectedPlayer.setBullets(Math.max(lives - 3, 0));
            /* if (affectedPlayer.getCharacterCard().getName().equals("Bart Cassidy")){          // Bart Cassidy Ability
                DeckService deckservice = new DeckService();
                deckservice.cassidyDraw(affectedPlayer); 
            }  */
        } else {
            affectedPlayer.getLeftNeighbor().getOnFieldCards().addOnFieldCard(this);
        }
        affectedPlayer.getOnFieldCards().removeOnFieldCard(this);
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}
