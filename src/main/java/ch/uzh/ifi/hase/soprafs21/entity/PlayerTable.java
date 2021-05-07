package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;

@Entity
@Table(name = "playertable")
public class PlayerTable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<Player> players;

    @OneToOne(cascade = CascadeType.ALL)
    private Player playerOnTurn;

    @OneToOne(cascade = CascadeType.ALL)
    private Deck deck;

    @OneToOne(cascade = CascadeType.ALL)
    private Deck discardPile;

    @OneToOne
    private VisibleCards visibleCards;

    @OneToOne(cascade = CascadeType.ALL)
    private CharacterPile characterPile;

    @Column
    private GameStatus gameStatus = GameStatus.PREPARATION;

    @Column
    private Long turnStart;

    @Column
    private Long timeRemaining = 60000L;

    @Column
    private Long maxTime = 60000L;

    public List<Player> getPlayersById(List<Long> playerIds) {
        List<Player> targetPlayers = new ArrayList<>();
        for (Long id : playerIds) {
            targetPlayers.add(getPlayerById(id).get());
        }
        return targetPlayers;

    }

    public List<Player> getPlayersInRangeOf(Long id) {
        Optional<Player> playerOpt = getPlayerById(id);
        if (!playerOpt.isPresent()) {
            throw new IllegalArgumentException("Player is not in this PlayerTable.");
        }
        Player rootPlayer = playerOpt.get();
        List<Player> playersInRange = new ArrayList<>();
        for (Player player : players) {
            if (rootPlayer.reachesWithWeapon(player)) {
                playersInRange.add(player);
            }
        }
        return playersInRange;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getPlayerOnTurn() {
        return playerOnTurn;
    }

    public void setPlayerOnTurn(Player playerOnTurn) {
        this.playerOnTurn = playerOnTurn;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(Deck discardPile) {
        this.discardPile = discardPile;
    }

    public CharacterPile getCharacterPile() {
        return characterPile;
    }

    public void setCharacterPile(CharacterPile characterPile) {
        this.characterPile = characterPile;
    }

    public Optional<Player> getPlayerById(Long id) {
        for (Player player : players) {
            if (id.equals(player.getId())) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public Player getPlayerByPlayerID(Long id) {
        for (Player player : players) {
            if (id.equals(player.getId())) {
                return player;
            }
        }
        return null;
    }

    public VisibleCards getVisibleCards() {
        return visibleCards;
    }

    public void setVisibleCards(VisibleCards visibleCards) {
        this.visibleCards = visibleCards;
    }

    public List<Player> getAlivePlayers() {
        List<Player> alivePlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getBullets() > 0) {
                alivePlayers.add(player);
            }
        }
        return alivePlayers;
    }

    public Long getTurnStart() {
        return turnStart;
    }

    public void setTurnStart(Long time) {
        this.turnStart = time;
    }

    public Long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(Long time) {
        this.timeRemaining = time;
    }

    public Long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Long time) {
        this.maxTime = time;
    }

}
