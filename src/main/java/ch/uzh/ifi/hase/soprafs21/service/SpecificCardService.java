package ch.uzh.ifi.hase.soprafs21.service;

import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;

@Service
public class SpecificCardService {

    @Autowired
    PlayerService playerService;

    @Autowired
    VisibleCardsService visibleCardsService;

    @Autowired
    DeckService deckService;

    public void use(PlayerTable table, PlayCard card, Player user, List<Player> targets) {
        switch (card.toString()) {
            case "Bang":
                bang(user, targets);
                break;
            case "Missed":
                missed();
                break;
            case "Beer":
                beer(user);
                break;
            case "General Store":
                generalStore(table, user, targets);
                break;
            case "Saloon":
                saloon(user, targets);
                break;
            case "Stage Coach":
                stagecoach(table, user);
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

    public void beer(Player activePlayer) {
        if(activePlayer.getBullets() < activePlayer.getMaxBullets()){
            activePlayer.setBullets(activePlayer.getBullets() + 1);
        } else {
            throw new UnsupportedOperationException("You already have the maximum amount of lives.");
        }
    }

    public void saloon(Player activePlayer, List<Player> otherPlayers) {

        if(activePlayer.getBullets() < activePlayer.getMaxBullets()){
            activePlayer.setBullets(activePlayer.getBullets() + 1);
        } else {
            throw new UnsupportedOperationException("You already have the maximum amount of lives.");
        }
    }

    public void generalStore(PlayerTable table, Player activePlayer, List<Player> otherPlayers) {
        // TODO: have the players pick the chosen card
        PlayCard randomCard = new Beer();
        visibleCardsService.pickACard(table, activePlayer, randomCard);
        Player currPlayer = activePlayer;
        for (int i = 0; i < otherPlayers.size(); i++) {
            Player newPlayer = currPlayer.getRightNeighbor();
            visibleCardsService.pickACard(table, newPlayer, randomCard);
        }
    }

    public void stagecoach(PlayerTable table, Player activePlayer){
        deckService.drawCards(table, activePlayer, 2);
    }
}
