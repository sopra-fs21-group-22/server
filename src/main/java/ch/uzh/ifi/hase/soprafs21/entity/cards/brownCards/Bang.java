package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;
import ch.uzh.ifi.hase.soprafs21.service.DeckService;


@Entity
public class Bang extends BrownCard {

    public Bang(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.BANG;
        this.priority = Priority.SIXTH;
    }

    public Bang() {
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload) {
        if (usingPlayer.getStillPlayableBangsThisRound() <= 0) {
            throw new GameLogicException("Can't play more BANG cards this round!");
        }
        target.takeHit(usingPlayer, this);
        /* if (target.getCharacterCard().getName().equals("Bart Cassidy")){         // Bart Cassidy Ability
            DeckService deckservice = new DeckService();
            deckservice.cassidyDraw(target); 
        } 
        if (target.getCharacterCard().getName().equals("El Gringo")){         // El Gringo Ability 
            DeckService deckservice = new DeckService();
            deckservice.gringoDraw(target, usingPlayer); 
        }  */
        usingPlayer.setStillPlayableBangsThisRound(usingPlayer.getStillPlayableBangsThisRound() - 1);
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.reachesWithWeapon(targetPlayer) && !usingPlayer.getId().equals(targetPlayer.getId());
    }

}
