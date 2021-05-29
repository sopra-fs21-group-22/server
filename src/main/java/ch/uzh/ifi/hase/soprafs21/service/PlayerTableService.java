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
import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs21.entity.CharacterPile;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.exceptions.IllegalGameStateException;
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

        PlayerTable playerTable = null;
        User user = userRepository.getOne(id);

        if (user.getPlayer() != null) {
            throw new GameLogicException("User is already in a game!");
        }

        List<PlayerTable> playerTables = playerTableRepository.findAll();
        // find playerT able
        for (PlayerTable table : playerTables) {
            List<Player> players = table.getPlayers();
            if (players.size() < 7 && table.getGameStatus() == GameStatus.PREPARATION) {
                playerTable = table;
            }
        }
        // create new playerTable if none has been found
        if (playerTable == null) {
            playerTable = createNewTable();
        }

        Player player = new Player();
        Hand hand = handService.createHand(player);
        player.setHand(hand);
        player.setTable(playerTable);
        // player.setCharacterCard(characterCardService.pickCharacter(player,
        // playerTable));
        playerTableRepository.save(playerTable);

        playerTable.getPlayers().add(player);

        playerRepository.saveAndFlush(player);
        playerTableRepository.save(playerTable);

        user.setPlayer(player);
        player.setUser(user);
        userRepository.save(user);
        userRepository.flush();

        return playerTable;
    }

    private PlayerTable createNewTable() {
        PlayerTable playerTable = new PlayerTable();

        List<Player> players = new ArrayList<>();
        Deck deck = deckService.createDeck();
        Deck discardPile = deckService.createDiscardPile();
        CharacterPile characterPile = characterCardService.createCharacterPile();
        List<Message> messages = new ArrayList<>();
        Chat chat = new Chat();

        Message message = new Message();
        message.setContent("Welcome to the Game!");
        message.setName("BANG!");
        messages.add(message);

        chat.setMessages(messages);
        deck.setDiscardPile(discardPile);

        playerTable.setPlayers(players);
        playerTable.setDeck(deck);
        playerTable.setDiscardPile(discardPile);
        playerTable.setCharacterPile(characterPile);
        playerTable.setChat(chat);

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

    public Player getPlayerById(Long id) {
        return playerRepository.getOne(id);
    }

    private void startGame(PlayerTable table) {
        table.setGameStatus(GameStatus.ONGOING);

        this.assignGameRoles(table);
        this.assignTablePositions(table);
        // assign first player on turn

        for (int i = 0; i < table.getPlayers().size(); i++) {
            Player currPlayer = table.getPlayers().get(i);
            currPlayer.setCharacterCard(characterCardService.pickCharacter(currPlayer, table));
            if (currPlayer.getGameRole().equals(GameRole.SHERIFF)) {
                currPlayer.setMaxBullets(currPlayer.getCharacterCard().getLifeAmount() + 1);
                currPlayer.setBullets(currPlayer.getCharacterCard().getLifeAmount() + 1);
                table.setPlayerOnTurn(currPlayer);
                startTurn(currPlayer, table);
                table.setTurnStart(System.currentTimeMillis());
            }
            deckService.drawCards(table, currPlayer, currPlayer.getBullets());

            currPlayer.setOnFieldCards(onFieldCardsService.createOnFieldCards(currPlayer));
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

        if (table.getGameStatus() != GameStatus.PREPARATION) {
            throw new IllegalGameStateException(table.getGameStatus());
        }

        Player player = playerRepository.getOne(playerId);
        if (!player.getTable().getId().equals(table.getId())) {
            throw new IllegalArgumentException(String.format("Player %s is not in PlayerTable with id %s.",
                    player.getUser().getUsername(), table.getId()));
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
        currPlayer.setStillPlayableBangsThisRound(currPlayer.getPlaybleBangsAnyRound());
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
        table.setTurnStart(System.currentTimeMillis());

        // next player's turn starts
        startTurn(nextPlayer, table);
    }

    private void startTurn(Player nextPlayer, PlayerTable table) {
        OnFieldCards onFieldCards = nextPlayer.getOnFieldCards();

        // go through all onFieldCards to check if they have a functionality at the
        // beginning of a turn
        int numCards = onFieldCards.getLength();

        for (int i = 0; i < onFieldCards.getLength(); i++) {
            BlueCard currCard = onFieldCards.get(i);
            currCard.onTurnStart(nextPlayer);
            if (onFieldCards.getLength() != numCards) {
                i -= (numCards - onFieldCards.getLength());
                numCards = onFieldCards.getLength();
            }
        }
        if (!nextPlayer.getId().equals(table.getPlayerOnTurn().getId())) {
            startTurn(table.getPlayerOnTurn(), table);
            return;
        }

        // TODO change to dynamic amount of cards
        deckService.drawCards(table, nextPlayer, 2);
    }

    public void checkGameState(Long gameId, GameStatus status) {
        PlayerTable table = getPlayerTableById(gameId);
        if (table.getGameStatus() != status) {
            throw new IllegalGameStateException(table.getGameStatus());
        }
    }

    public void updateTimer(PlayerTable table) {
        table.setTimeRemaining(table.getMaxTime() - (System.currentTimeMillis() - table.getTurnStart()));

        // time runs out and it is not the 3rd strike
        // a strike gets added and the timer is reset
        if (table.getTimeRemaining() < 0L && table.getPlayerOnTurn().getStrikes() < 2) {
            table.getPlayerOnTurn().setStrikes(table.getPlayerOnTurn().getStrikes() + 1);
            table.setTurnStart(System.currentTimeMillis());
        }

        // time runs out and its the 3rd strike
        // a strike is added, all bullets, handcards and fieldcards are removed
        // the next player can start their turn
        else if (table.getTimeRemaining() < 0L) {
            table.getPlayerOnTurn().setStrikes(table.getPlayerOnTurn().getStrikes() + 1);

            table.getPlayerOnTurn().setBullets(0);
            /*
             * List<PlayCard> handCards = table.getPlayerOnTurn().getHand().getPlayCards();
             * List<BlueCard> onFieldCards =
             * table.getPlayerOnTurn().getOnFieldCards().getOnFieldCards();
             * 
             * for (PlayCard card : handCards) {
             * table.getPlayerOnTurn().getTable().getDiscardPile().addCard(card); } for
             * (PlayCard card : onFieldCards) {
             * table.getPlayerOnTurn().getTable().getDiscardPile().addCard(card); }
             * table.getPlayerOnTurn().getHand().setPlayCards(new ArrayList<>());
             * table.getPlayerOnTurn().getOnFieldCards().removeAllCards();
             */
            if (table.getGameStatus() == GameStatus.ONGOING) {
                nextPlayersTurn(table);
            }
        }
    }

    public void changeTimer(PlayerTable table, Long time) {
        table.setTimeRemaining(time);
        table.setMaxTime(time);
    }

    public void addMessage(PlayerTable table, String content, String name) {
        Message message = new Message();
        message.setContent(content);
        message.setName(name);
        table.getChat().addMessage(message);
        playerTableRepository.saveAndFlush(table);
    }
}
