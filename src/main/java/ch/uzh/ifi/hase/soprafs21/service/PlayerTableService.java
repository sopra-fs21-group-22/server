package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.entity.CharacterPile;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
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

    @Autowired
    CharacterCardService characterCardService;

    @Autowired
    OnFieldCardsService onFieldCardsService;

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
                player.setCharacterCard(characterCardService.pickCharacter(player, playerTable));
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
        CharacterPile characterPile = characterCardService.createCharacterPile();
        players.add(player);
        player.setTable(playerTable);
        playerTable.setPlayers(players);
        playerTable.setDeck(deck);
        playerTable.setDiscardPile(discardPile);
        playerTable.setCharacterPile(characterPile);
        playerTableRepository.save(playerTable);
        player.setCharacterCard(characterCardService.pickCharacter(player, playerTable));
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
            Player currPlayer = table.getPlayers().get(i);
            if (currPlayer.getGameRole().equals(GameRole.SHERIFF)) {
                table.setPlayerOnTurn(currPlayer);
            }
            deckService.drawCards(table, currPlayer, currPlayer.getBullets());

            currPlayer.setOnFieldCards(onFieldCardsService.createOnFieldCards());
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
        // End current turn
        Player currPlayer = table.getPlayerOnTurn();
        if (currPlayer.getHand().getPlayCards().size() > currPlayer.getBullets()) {
            throw new GameLogicException(
                    "Too many cards in Hand! Discard until there are not more cards left than lives you have!");
        }

        // move turn to player to the right, skip dead players
        while (currPlayer.getRightNeighbor().getBullets() <= 0) {
            currPlayer = currPlayer.getRightNeighbor();
        }
        Player nextPlayer = currPlayer.getRightNeighbor();
        table.setPlayerOnTurn(nextPlayer);

        // next player's turn starts
        startTurn(nextPlayer, table);

    }

    public void startTurn(Player nextPlayer, PlayerTable table){
        OnFieldCards onFieldCards = nextPlayer.getOnFieldCards();

        // go through all onFieldCards to check if they have a functionality at the beginning of a turn
        for (int i = 0; i < onFieldCards.getLength(); i++) {
            BlueCard currCard = onFieldCards.get(i);
            currCard.onTurnStart(nextPlayer);
        }

        // TODO change to dynamic amount of cards
        deckService.drawCards(table, nextPlayer, 2);
    }

}
