package ch.uzh.ifi.hase.soprafs21.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;

@Service
public class SpecificCardService {

    @Autowired
    PlayerService playerService;

    public void use(PlayCard card, Player user, List<Player> targets) {
        switch (card.toString()) {
        case "Bang":
            bang(user, targets);
            break;
        case "Missed":
            missed();
            break;
        default:
            throw new IllegalArgumentException(String.format("Card %s does not exist!", card.getClass().toString()));

        }
    }

    public void bang(Player user, List<Player> targets) {
        if (targets.size() != 1) {
            throw new IllegalArgumentException("A BANG card can only hit one target!");
        }
        playerService.attackPlayer(user, targets.get(0));
    }

    public void missed() {
        // TODO
    }
}
