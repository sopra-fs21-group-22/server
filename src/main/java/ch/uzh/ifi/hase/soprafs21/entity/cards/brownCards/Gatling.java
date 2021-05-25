package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;
import ch.uzh.ifi.hase.soprafs21.service.DeckService;

@Entity
public class Gatling extends BrownCard {
    public Gatling() {
    }

    public Gatling(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.GATLING;
        this.priority = Priority.TENTH;

    }

    @Override
    protected void onPlacement(Player usingPlayer, Player targetPlayer, PayLoadDTO payload) {
        List<Player> alivePlayers = usingPlayer.getTable().getAlivePlayers();
        for (int i = 0; i < alivePlayers.size(); i++) {
            Player target = alivePlayers.get(i);
            if (target.getId().equals(usingPlayer.getId())) {
                continue;
            }
            target.takeHit(usingPlayer, this);
            /*
             * if (target.getCharacterCard().getName().equals("Bart Cassidy")){ // Bart
             * Cassidy Ability DeckService deckservice = new DeckService();
             * deckservice.cassidyDraw(target); } if
             * (target.getCharacterCard().getName().equals("El Gringo")){ // El Gringo
             * Ability DeckService deckservice = new DeckService();
             * deckservice.gringoDraw(target, usingPlayer); }
             */
        }
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}
