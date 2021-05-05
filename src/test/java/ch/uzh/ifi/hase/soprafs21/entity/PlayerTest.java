package ch.uzh.ifi.hase.soprafs21.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Barrel;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Schofield;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;

public class PlayerTest {

    private List<Player> players;

    @BeforeEach
    public void beforeEach() {
        PlayerTable table = new PlayerTable();
        Deck deck = new Deck();
        Deck discardPile = new Deck();
        table.setDiscardPile(discardPile);
        table.setDeck(deck);
        for (int i = 0; i < 50; i++) {
            deck.addCard(new Bang(Rank.SEVEN, Suit.SPADES));
        }

        players = new ArrayList<>();
        Player oldPlayer = new Player();
        oldPlayer.setId(15L);
        players.add(oldPlayer);
        oldPlayer.setOnFieldCards(new OnFieldCards());
        oldPlayer.setHand(new Hand());
        oldPlayer.setTable(table);

        for (int i = 0; i < 7; i++) {
            Player newPlayer = new Player();
            newPlayer.setId(Long.valueOf(i));
            newPlayer.setOnFieldCards(new OnFieldCards());
            newPlayer.setHand(new Hand());
            newPlayer.setTable(table);
            players.add(newPlayer);
            newPlayer.setRightNeighbor(oldPlayer);
            oldPlayer.setLeftNeighbor(newPlayer);
            oldPlayer = newPlayer;
        }
        table.setPlayers(players);
        Player firstPlayer = players.get(0);
        Player lastPlayer = players.get(players.size() - 1);
        firstPlayer.setRightNeighbor(lastPlayer);
        lastPlayer.setLeftNeighbor(firstPlayer);
    }

    @Test
    public void testWeaponRange() {
        Player player = players.get(0);
        assertTrue(player.reachesWithWeapon(player.getRightNeighbor()));
        assertTrue(player.reachesWithWeapon(player.getLeftNeighbor()));
    }

    @Test
    public void testWeaponRange_changedRange_inRange() {
        Player player = players.get(0);
        player.setRange(3);
        assertTrue(player.reachesWithWeapon(player.getLeftNeighbor().getLeftNeighbor().getLeftNeighbor()));
    }

    @Test
    public void testWeaponRange_changedRange_outOfRange() {
        Player player = players.get(0);
        player.setRange(3);
        assertFalse(player
                .reachesWithWeapon(player.getLeftNeighbor().getLeftNeighbor().getLeftNeighbor().getLeftNeighbor()));
    }

    @Test
    public void testWeaponRange_distanceToOthers_inRange() {
        Player player = players.get(0);
        player.setRange(1);
        player.setDistanceDecreaseToOthers(2);
        assertTrue(player.reachesWithWeapon(player.getRightNeighbor().getRightNeighbor().getRightNeighbor()));
    }

    @Test
    public void testWeaponRange_distanceToOthers_outOfRange() {
        Player player = players.get(0);
        player.setRange(1);
        player.setDistanceDecreaseToOthers(2);
        Player target = player.getRightNeighbor().getRightNeighbor().getRightNeighbor().getRightNeighbor();
        assertFalse(player.reachesWithWeapon(target));
    }

    @Test
    public void testWeaponRange_distanceForOthers_inRange() {
        Player player = players.get(0);
        player.setRange(3);
        Player target = player.getRightNeighbor();
        target.setDistanceIncreaseForOthers(2);
        assertTrue(player.reachesWithWeapon(target));
    }

    @Test
    public void testWeaponRange_distanceForOthers_outOfRange() {
        Player player = players.get(0);
        player.setRange(3);
        Player target = player.getRightNeighbor();
        target.setDistanceIncreaseForOthers(3);
        assertFalse(player.reachesWithWeapon(target));
    }

    @Test
    public void testOnDeath_Outlaw_drawCards() {

        Player outlaw = players.get(0);
        Player killer = players.get(1);
        outlaw.setBullets(1);
        outlaw.setGameRole(GameRole.OUTLAW);
        killer.setGameRole(GameRole.SHERIFF);

        int currAmountHandCards = killer.getHand().getLength();
        int expectedAmountHandCards = currAmountHandCards + 3;

        outlaw.takeHit(killer);

        assertEquals(expectedAmountHandCards, killer.getHand().getLength());
    }

    @Test
    public void testOnDeath_Sheriff_GameOver() {
        Player outlaw = players.get(0);
        Player sheriff = players.get(1);

        outlaw.setGameRole(GameRole.OUTLAW);
        sheriff.setGameRole(GameRole.SHERIFF);

        sheriff.setBullets(1);

        sheriff.takeHit(outlaw);

        assertEquals(GameStatus.ENDED, sheriff.getTable().getGameStatus());
    }

    @Test
    public void testOnDeath_DeputyBySheriff() {
        Player deputy = players.get(0);
        Player sheriff = players.get(1);

        deputy.setGameRole(GameRole.DEPUTY);
        sheriff.setGameRole(GameRole.SHERIFF);

        List<PlayCard> cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cards.add(new Bang(Rank.SEVEN, Suit.SPADES));
        }
        sheriff.getOnFieldCards().addOnFieldCard(new Schofield());
        sheriff.getOnFieldCards().addOnFieldCard(new Barrel());
        sheriff.getHand().addCards(cards);

        deputy.setBullets(1);
        deputy.takeHit(sheriff);

        assertEquals(0, sheriff.getHand().getLength());
        assertEquals(0, sheriff.getOnFieldCards().getOnFieldCards().size());
    }

    @Test
    public void testOnDeath_LastPlayer() {
        for (int i = 2; i < players.size(); i++) {
            players.get(i).setBullets(0);
        }
        Player killer = players.get(0);
        Player target = players.get(1);
        target.setBullets(1);

        target.takeHit(killer);

        assertEquals(GameStatus.ENDED, killer.getTable().getGameStatus());

    }

}
