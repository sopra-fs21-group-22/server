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
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Appaloosa;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Barrel;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Schofield;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;
import ch.uzh.ifi.hase.soprafs21.entity.gameMoves.GameMove;

public class PlayerTest {

    private List<Player> players;
    private List<GameRole> gameRoles = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        gameRoles.add(GameRole.SHERIFF);
        gameRoles.add(GameRole.OUTLAW);
        gameRoles.add(GameRole.OUTLAW);
        gameRoles.add(GameRole.RENEGADE);
        gameRoles.add(GameRole.DEPUTY);
        gameRoles.add(GameRole.OUTLAW);
        gameRoles.add(GameRole.DEPUTY);

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
        User user = new User();
        user.setUsername("Ada");
        oldPlayer.setUser(user);
        oldPlayer.setId(15L);
        players.add(oldPlayer);
        oldPlayer.setOnFieldCards(new OnFieldCards());
        oldPlayer.setGameRole(gameRoles.get(0));
        oldPlayer.setHand(new Hand());
        oldPlayer.setTable(table);

        table.setPlayerOnTurn(players.get(0));

        for (int i = 0; i < 6; i++) {
            Player newPlayer = new Player();
            user = new User();
            user.setUsername("Ada");
            newPlayer.setUser(user);
            newPlayer.setGameRole(gameRoles.get(i + 1));
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
        player.setRange(2);
        assertFalse(player.reachesWithWeapon(player.getLeftNeighbor().getLeftNeighbor().getLeftNeighbor()));
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
        player.setDistanceDecreaseToOthers(1);
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
        CharacterCard characterCard = new CharacterCard();
        characterCard.setLifeAmount(3);
        characterCard.setName("Paul Regret");
        Player outlaw = players.get(0);
        Player killer = players.get(1);
        outlaw.setCharacterCard(characterCard);
        outlaw.setBullets(1);
        outlaw.setGameRole(GameRole.OUTLAW);
        killer.setGameRole(GameRole.SHERIFF);

        int currAmountHandCards = killer.getHand().getLength();
        int expectedAmountHandCards = currAmountHandCards + 3;

        outlaw.takeHit(killer, null);

        assertEquals(expectedAmountHandCards, killer.getHand().getLength());
    }

    @Test
    public void testOnDeath_Sheriff_GameOver() {
        CharacterCard characterCard = new CharacterCard();
        characterCard.setLifeAmount(3);
        characterCard.setName("Paul Regret");
        Player outlaw = players.get(0);
        Player sheriff = players.get(1);
        sheriff.setCharacterCard(characterCard);

        outlaw.setGameRole(GameRole.OUTLAW);
        sheriff.setGameRole(GameRole.SHERIFF);

        sheriff.setBullets(1);

        sheriff.takeHit(outlaw, null);

        assertEquals(GameStatus.ENDED, sheriff.getTable().getGameStatus());
    }

    @Test
    public void testOnDeath_OutlawsWin() {
        Player sheriff = players.get(0);
        sheriff.setBullets(0);
        assertEquals(GameStatus.ENDED, sheriff.getTable().getGameStatus());
        GameMove winnerDeclaration = sheriff.getTable().getNewestGameMove();
        assertTrue(winnerDeclaration.getMessage().toLowerCase().contains("outlaw"));
    }

    @Test
    public void testOnDeath_renegadeWins() {
        for (int i = 0; i < 7; i++) {
            Player player = players.get(i);
            if (player.getGameRole().equals(GameRole.SHERIFF) || player.getGameRole().equals(GameRole.RENEGADE)) {
                continue;
            }
            player.setBullets(0);
        }

        Player sheriff = players.get(0);
        sheriff.setBullets(0);
        GameMove winnerDeclaration = sheriff.getTable().getNewestGameMove();
        assertTrue(winnerDeclaration.getMessage().toLowerCase().contains("renegade"));

    }

    @Test
    public void testOnDeath_DeputyBySheriff() {
        CharacterCard characterCard = new CharacterCard();
        characterCard.setLifeAmount(3);
        characterCard.setName("Paul Regret");
        Player deputy = players.get(0);
        Player sheriff = players.get(1);
        deputy.setCharacterCard(characterCard);

        deputy.setGameRole(GameRole.DEPUTY);
        sheriff.setGameRole(GameRole.SHERIFF);

        List<PlayCard> cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cards.add(new Bang(Rank.SEVEN, Suit.SPADES));
        }
        sheriff.getOnFieldCards().addOnFieldCard(new Schofield(Rank.SEVEN, Suit.SPADES));
        sheriff.getOnFieldCards().addOnFieldCard(new Barrel(Rank.SEVEN, Suit.HEARTS));
        sheriff.getHand().addCards(cards);

        deputy.setBullets(1);
        deputy.takeHit(sheriff, null);

        assertEquals(0, sheriff.getHand().getLength());
        assertEquals(0, sheriff.getOnFieldCards().getOnFieldCards().size());
    }

    @Test
    public void testOnDeath_LastPlayer() {
        CharacterCard characterCard = new CharacterCard();
        characterCard.setLifeAmount(3);
        characterCard.setName("Paul Regret");
        for (int i = 2; i < players.size(); i++) {
            players.get(i).setBullets(0);
        }
        Player killer = players.get(0);
        Player target = players.get(1);
        target.setCharacterCard(characterCard);
        target.setBullets(1);

        target.takeHit(killer, null);

        assertEquals(GameStatus.ENDED, killer.getTable().getGameStatus());
    }

    @Test
    public void testPlayCard() {
        Player player = players.get(0);
        PlayCard card = new Bang(Rank.SEVEN, Suit.SPADES);
        card.setId(1212512L);
        player.getHand().addCard(card);
        player.playCard(card.getId(), player.getLeftNeighbor(), null);
        assertEquals(0, player.getHand().getLength());
    }

    @Test
    public void testSetBulletsZero_removesPlayerCards() {
        Player player = players.get(0);
        for (int i = 0; i < 3; i++) {
            player.getHand().addCard(new Bang(Rank.NINE, Suit.DIAMONDS));
        }
        player.getOnFieldCards().addOnFieldCard(new Schofield(Rank.TEN, Suit.CLUBS));
        player.getOnFieldCards().addOnFieldCard(new Appaloosa(Rank.TEN, Suit.CLUBS));
        player.getOnFieldCards().addOnFieldCard(new Barrel(Rank.TEN, Suit.CLUBS));
        player.setBullets(0);
        assertEquals(0, player.getOnFieldCards().getOnFieldCards().size());
        assertEquals(0, player.getHand().getPlayCards().size());
    }

    @Test
    public void testOnDeath_removesPlayerCards() {
        Player player = players.get(0);
        for (int i = 0; i < 3; i++) {
            player.getHand().addCard(new Bang(Rank.NINE, Suit.DIAMONDS));
        }
        player.getOnFieldCards().addOnFieldCard(new Schofield(Rank.TEN, Suit.CLUBS));
        player.getOnFieldCards().addOnFieldCard(new Appaloosa(Rank.TEN, Suit.CLUBS));
        player.getOnFieldCards().addOnFieldCard(new Barrel(Rank.TEN, Suit.CLUBS));
        player.onDeath();
        assertEquals(0, player.getOnFieldCards().getOnFieldCards().size());
        assertEquals(0, player.getHand().getPlayCards().size());
    }

}
