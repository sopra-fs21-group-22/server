package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;

@Entity
public abstract class BlueCard extends PlayCard {

    @Override
    public String getColor() {
        return "blue";
    }

    @Override
    public void use(Player usingPlayer, List<Player> targets) {
        // TODO Auto-generated method stub

    }

    public abstract void use(Player usingPlayer, Player targetPlayer);

    public abstract void onTurnStart(Player affectedPlayer);

    public abstract void undo(Player affectedPlayer);

    public abstract void onHit(Player affectedPlayer);
}