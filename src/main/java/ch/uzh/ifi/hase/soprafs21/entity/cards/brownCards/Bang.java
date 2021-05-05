package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.service.DeckService;

@Entity
public class Bang extends BrownCard {

    public Bang(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.BANG;
    }

    public Bang() {
    }

    @Override
    protected void onPlacement(Player usingPlayer, List<Player> targets) {
        if (usingPlayer.getStillPlayableBangsThisRound() <= 0) {
            throw new GameLogicException("Can't play more BANG cards this round!");
        }
        Player target = targets.get(0);

        if (!(usingPlayer.reachesWithWeapon(target))) {
            throw new GameLogicException("Player is out of range!");
        }

        target.setBullets(target.getBullets() - 1);
        /*  if (target.getCharacterCard().getName().equals("Bart Cassidy")){         // BArt Cassidy Ability
            DeckService deckservice = new DeckService();
            deckservice.cassidyDraw(target); 
        }  */
        usingPlayer.setStillPlayableBangsThisRound(usingPlayer.getStillPlayableBangsThisRound() - 1);
    }
}
