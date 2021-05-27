package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.Schofield;
import ch.uzh.ifi.hase.soprafs21.entity.gameMoves.GameMove;

public class IndiansTest {

    private Indians indians = new Indians(Rank.ACE, Suit.CLUBS);
    private List<Player> players;
    private PlayerTable table = new PlayerTable();

    @BeforeEach
    public void beforeEach() {
        Deck deck = new Deck();
        table.setDeck(deck);

        // create a game with 7 players and their Hand & onField Cards
        players = new ArrayList<>();
        Player oldPlayer = new Player();
        User user = new User();
        user.setUsername("Ada");
        oldPlayer.setUser(user);
        oldPlayer.setId(15L);
        oldPlayer.setTable(table);
        table.setPlayerOnTurn(oldPlayer); // players.get(0) onTurn
        table.setDiscardPile(new Deck());
        table.setPlayers(players);
        players.add(oldPlayer);
        oldPlayer.setOnFieldCards(new OnFieldCards());
        oldPlayer.setHand(new Hand());

        for (int i = 0; i < 6; i++) {
            Player newPlayer = new Player();
            user = new User();
            user.setUsername("Ada");
            newPlayer.setUser(user);
            newPlayer.setId(Long.valueOf(i));
            newPlayer.setOnFieldCards(new OnFieldCards());
            newPlayer.setHand(new Hand());
            newPlayer.setTable(table);
            players.add(newPlayer);
            newPlayer.setRightNeighbor(oldPlayer);
            oldPlayer.setLeftNeighbor(newPlayer);
            oldPlayer = newPlayer;
        }
        Player firstPlayer = players.get(0);
        Player lastPlayer = players.get(players.size() - 1);
        firstPlayer.setRightNeighbor(lastPlayer);
        lastPlayer.setLeftNeighbor(firstPlayer);
    }

    @Test
    public void testTargetIsValid() {
        Player user = players.get(0);
        assertTrue(indians.targetIsValid(user, user));
    }

    @Test
    public void bangHolderNoDMG() {
        Player user = players.get(0);
        Player targetWithBang = user.getRightNeighbor();
        targetWithBang.getHand().getPlayCards().add(new Bang(Rank.ACE, Suit.SPADES));
        int expectedLives = targetWithBang.getBullets();

        indians.use(user, user, null);
        assertEquals(expectedLives, targetWithBang.getBullets());
    }

    @Test
    public void noBangHolderTakesDMG() {
        Player user = players.get(0);
        Player targetWithoutBang = user.getLeftNeighbor();
        int expectedLives = targetWithoutBang.getBullets() - 1;

        indians.use(user, user, null);
        assertEquals(expectedLives, targetWithoutBang.getBullets());
    }

    @Test
    public void bangHolderLosesBang() {
        Player user = players.get(0);
        Player targetWithBang = user.getRightNeighbor();
        targetWithBang.getHand().getPlayCards().add(new Bang(Rank.ACE, Suit.SPADES));
        targetWithBang.getHand().getPlayCards().add(new Schofield(Rank.ACE, Suit.SPADES));

        indians.use(user, user, null);
        assertEquals(1, targetWithBang.getHand().getPlayCards().size());
    }

    @Test
    public void doesntHitUser() {
        Player user = players.get(0);
        int expectedLives = user.getBullets();

        indians.use(user, user, null);
        assertEquals(expectedLives, user.getBullets());
    }

    @Test
    public void gameMovesAdded() {
        Player user = players.get(0);
        Player targetWithBang = user.getRightNeighbor();
        targetWithBang.getHand().getPlayCards().add(new Bang(Rank.ACE, Suit.SPADES));
        targetWithBang.getHand().getPlayCards().add(new Schofield(Rank.ACE, Suit.SPADES));

        indians.use(user, user, null);
        List<GameMove> moves = user.getTable().getGameMoves().subList(0, 6);

        GameMove moveWithTarget = new GameMove();

        for (GameMove move : moves) {
            if (move.getTargetPlayer().getId().equals(targetWithBang.getId())) {
                moveWithTarget = move;
            }
        }
        assertEquals(GameMoveAction.FAIL, moveWithTarget.getAction());
        assertEquals(targetWithBang.getId(), moveWithTarget.getTargetPlayer().getId());
        assertEquals(Card.INDIANS, moveWithTarget.getCard().getCard());
    }

}
