package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

@Entity
public class Saloon extends BrownCard {
    public Saloon() {
    }

    public Saloon(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.SALOON;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player target, PayLoadDTO payload) {
        List<Player> players = usingPlayer.getTable().getAlivePlayers();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.setBullets(player.getBullets() + 1);
        }
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}
