package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.HandRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

@Service
@Transactional
public class PlayerTableService {

    @Autowired
    PlayerTableRepository playerTableRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DeckRepository deckRepository;

    @Autowired
    HandRepository handRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameRoleService gameRoleService;

    @Autowired
    HandService handService;

    @Autowired
    DeckService deckService;

    /**
     * Creates and adds a <Player> to a preexisting or new <PlayerTable>.
     * 
     * @param id user id
     */
    public PlayerTable addPlayer(Long id) {
        // check if user is already in a game
        if (playerRepository.existsById(id)) {
            throw new IllegalArgumentException("User is already in a game!");
        }

        Player player = new Player();
        Hand hand = handService.createHand();
        User user = userRepository.getOne(id);
        player.setUser(user);
        player.setHand(hand);
        player.setId(user.getId());
        List<PlayerTable> playerTables = playerTableRepository.findAll();
        // add user to existing playerTable
        for (PlayerTable playerTable : playerTables) {
            List<Player> players = playerTable.getPlayers();
            if (players.size() < 7 && !playerTable.getGameHasStarted()) {

                players.add(player);
                playerTable.setPlayers(players);
                player.setTable(playerTable);

                playerTableRepository.save(playerTable);
                playerTableRepository.flush();

                return playerTable;
            }
        }
        // create new playerTable
        PlayerTable playerTable = new PlayerTable();
        List<Player> players = new ArrayList<>();
        Deck deck = deckService.createDeck();
        Deck discardPile = deckService.createDiscardPile();
        players.add(player);
        player.setTable(playerTable);
        playerTable.setPlayers(players);
        playerTable.setDeck(deck);
        playerTable.setDiscardPile(discardPile);
        playerTableRepository.save(playerTable);
        playerTableRepository.flush();
        return playerTable;
    }

    // public void removePlayer(PlayerTable table, Long playerId) {
    // Player playerToBeRemoved = playerRepository.getOne(playerId);
    // for (Player player : table.getPlayers()) {
    // if (player.getTablePosition() > playerToBeRemoved.getTablePosition()) {
    // player.setTablePosition(player.getTablePosition() - 1);
    // }
    // }
    // playerTableRepository.save(table);
    // }

    public PlayerTable getPlayerTableById(Long id) {
        return playerTableRepository.getOne(id);
    }

    private void startGame(PlayerTable table) {
        table.setGameHasStarted(true);

        this.assignGameRoles(table);
        this.assignTablePositions(table);
        // assign first player on turn

        for (int i = 0; i < table.getPlayers().size(); i++) {
            if (table.getPlayers().get(i).getGameRole().equals(GameRole.SHERIFF)) {
                table.setPlayerOnTurn(table.getPlayers().get(i));
            }
            deckService.drawCards(table, table.getPlayers().get(i), table.getPlayers().get(i).getBullets());
        }

        playerTableRepository.saveAndFlush(table);
    }

    private void assignGameRoles(PlayerTable table) {
        List<Player> players = table.getPlayers();
        List<GameRole> roles = gameRoleService.getRoles(players.size());
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setGameRole(roles.get(i));
        }
    }

    /**
     * Sets player's ready status and starts game if all players are ready
     * 
     * @param gameId
     * @param playerId
     * @param status   Boolean ready or not
     */
    public void setPlayerAsReady(Long gameId, Long playerId, Boolean status) {
        PlayerTable table = getPlayerTableById(gameId);
        Player player = playerRepository.getOne(playerId);
        if (!player.getTable().getId().equals(table.getId())) {
            throw new IllegalArgumentException(String.format("Player %s is not in PlayerTable with id %s.",
                    player.getUser().getUsername(), table.getId()));
        }
        if (table.getGameHasStarted()) {
            throw new IllegalArgumentException("Game has already started!");
        }
        player.setReady(status);

        Boolean playersReady = true;
        for (Player pl : table.getPlayers()) {
            if (!pl.getReady()) {
                playersReady = false;
                break;
            }
        }
        if (playersReady && table.getPlayers().size() >= 4) {
            startGame(table);
        }
    }

    private void assignTablePositions(PlayerTable table) {
        List<Player> players = table.getPlayers();
        Collections.shuffle(players);

        for (int i = 0; i < players.size(); i++) {
            int nextPlayerIdx = (i + 1) % (players.size());
            Player currPlayer = players.get(i);
            Player nextPlayer = players.get(nextPlayerIdx);

            currPlayer.setRightNeighbor(nextPlayer);
            nextPlayer.setLeftNeighbor(currPlayer);
            playerRepository.save(currPlayer);
            playerRepository.save(nextPlayer);
        }
    }

    public void removePlayer(PlayerTable table, Long playerId) {
        // TODO
    }

    public void nextPlayersTurn(PlayerTable table) {
        // TODO check amount of hand cards
        // TODO End turn
        // TODO start next turn
        Player currPlayer = table.getPlayerOnTurn();
        // TODO uncomment this
        // if (currPlayer.getHand().getPlayCards().size() > currPlayer.getBullets()) {
        // throw new GameLogicException(
        // "Too many cards in Hand! Discard until there are not more cards left than
        // lives you have!");
        // }

        // skip dead players
        while (currPlayer.getRightNeighbor().getBullets() <= 0) {
            currPlayer = currPlayer.getRightNeighbor();
        }
        Player nextPlayer = currPlayer.getRightNeighbor();
        table.setPlayerOnTurn(nextPlayer);
        // TODO change to dynamic amount of cards

        deckService.drawCards(table, nextPlayer, 2);
    }

}
