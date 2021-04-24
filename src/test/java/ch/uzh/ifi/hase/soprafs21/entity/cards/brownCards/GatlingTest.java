// package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.ArrayList;
// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
// import ch.uzh.ifi.hase.soprafs21.entity.Player;
// import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
// import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

// public class GatlingTest {

// private Player user;
// private List<Player> targets;
// private List<Player> players;
// private Gatling gatling = new Gatling();

// @BeforeEach
// public void beforeEach() {
// targets = new ArrayList<>();
// players = new ArrayList<>();
// Player oldPlayer = new Player();
// oldPlayer.setId(15L);
// players.add(oldPlayer);
// oldPlayer.setOnFieldCards(new OnFieldCards());

// for (int i = 0; i < 6; i++) {
// Player newPlayer = new Player();
// newPlayer.setId(Long.valueOf(i));
// newPlayer.setOnFieldCards(new OnFieldCards());
// players.add(newPlayer);
// newPlayer.setRightNeighbor(oldPlayer);
// oldPlayer.setLeftNeighbor(newPlayer);
// oldPlayer = newPlayer;
// }
// Player firstPlayer = players.get(0);
// Player lastPlayer = players.get(players.size() - 1);
// firstPlayer.setRightNeighbor(lastPlayer);
// lastPlayer.setLeftNeighbor(firstPlayer);

// user = firstPlayer;
// }

// @Test
// public void cantHitCardOwner() {
// targets.add(user);

// int expectedLives = user.getBullets();

// gatling.use(user, targets);

// int actualLives = user.getBullets();

// assertEquals(expectedLives, actualLives);
// }

// @Test
// public void hitsEveryone() {
// List<Integer> expectedLives = new ArrayList<>();
// for (Player target : targets) {
// expectedLives.add(target.getBullets() - 1);
// }
// gatling.use(user, targets);

// List<Integer> actualLives = new ArrayList<>();
// for (Player target : targets) {
// actualLives.add(target.getBullets());
// }

// for (Integer lives : actualLives) {
// expectedLives.remove(lives);
// }
// assertTrue(expectedLives.size() == 0);
// }
// }
