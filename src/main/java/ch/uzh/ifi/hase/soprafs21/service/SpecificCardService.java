package ch.uzh.ifi.hase.soprafs21.service;

import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Service
public class SpecificCardService {

    @Autowired
    PlayerService playerService;

    @Autowired
    VisibleCardsService visibleCardsService;

    @Autowired
    DeckService deckService;

    public void use(PlayerTable table, PlayCard card, Player user, List<Player> targets) {
        switch (card.getCard()) {
        case BANG:
            bang(user, targets);
            break;
        case BEER:
            beer(user);
            break;
        // case "General Store":
        // generalStore(table, user, targets);
        // break;
        case SALOON:
            saloon(user, targets);
            break;
        case STAGECOACH:
            stageCoach(table, user);
            break;
        case WELLSFARGO:
            wellsFargo(table, user);
            break;
        case GATLING:
            gatling(user, targets);
            break;

        default:
            throw new IllegalArgumentException(String.format("Card %s does not exist!", card.toString()));
        }

    }

    public void bang(Player user, List<Player> targets) {
        if (targets.size() != 1) {
            throw new IllegalArgumentException("A BANG card can only hit one target!");
        }
        if (targets.get(0).getBullets() == 0) {
            throw new IllegalArgumentException("A dead player may no longer be attacked.");
        }
        if (user.getBullets() == 0) {
            throw new IllegalArgumentException("A dead player may no longer play.");
        }
        playerService.attackPlayer(user, targets.get(0));
    }

    public void missed() {
        // TODO

    }

    public void beer(Player activePlayer) {
        if (activePlayer.getBullets() == 0) {
            throw new IllegalArgumentException("A dead player may no longer play.");
        }
        if (activePlayer.getBullets() < activePlayer.getMaxBullets()) {
            activePlayer.setBullets(activePlayer.getBullets() + 1);
        } else {
            throw new UnsupportedOperationException("You already have the maximum amount of lives.");
        }
    }

    public void saloon(Player activePlayer, List<Player> otherPlayers) {
        if (activePlayer.getBullets() == 0) {
            throw new IllegalArgumentException("A dead player may no longer play.");
        }
        if (activePlayer.getBullets() < activePlayer.getMaxBullets()) {
            activePlayer.setBullets(activePlayer.getBullets() + 1);
        } else {
            throw new UnsupportedOperationException("You already have the maximum amount of lives.");
        }
    }

    // public void generalStore(PlayerTable table, Player activePlayer, List<Player>
    // otherPlayers) {
    // // TODO: have the players pick the chosen card
    // PlayCard randomCard = new Beer();
    // visibleCardsService.pickACard(table, activePlayer, randomCard);
    // Player currPlayer = activePlayer;
    // for (int i = 0; i < otherPlayers.size(); i++) {
    // Player newPlayer = currPlayer.getRightNeighbor();
    // visibleCardsService.pickACard(table, newPlayer, randomCard);
    // }
    // }

    public void stageCoach(PlayerTable table, Player activePlayer) {
        if (activePlayer.getBullets() == 0) {
            throw new IllegalArgumentException("A dead player may no longer play.");
        }
        deckService.drawCards(table, activePlayer, 2);
    }

    public void wellsFargo(PlayerTable table, Player activePlayer) {
        if (activePlayer.getBullets() == 0) {
            throw new IllegalArgumentException("A dead player may no longer play.");
        }
        deckService.drawCards(table, activePlayer, 3);
    }

    public void gatling(Player activePlayer, List<Player> targets){
        if (activePlayer.getBullets() == 0) {
            throw new IllegalArgumentException("A dead player may no longer play.");
        }
        for (Integer i=0; i<targets.size(); i++) {      
            if (targets.get(i).getBullets() == 0) {
                throw new IllegalArgumentException("A dead player may no longer be attacked.");
            }
        }  
            playerService.attackAll(targets);
    }
}
