package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class PlayerTable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "playertable_id")
    private List<Player> players;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "playertable_id")
    private Player playerOnTurn;

    @OneToOne(cascade = CascadeType.ALL)
    private Deck deck;

    @OneToOne(cascade = CascadeType.ALL)
    private Deck discardPile;

    @OneToOne
    private VisibleCards visibleCards;

    @Column
    private Boolean gameHasStarted = false;

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

    public Boolean getGameHasStarted() {
        return gameHasStarted;
    }

    public void setGameHasStarted(Boolean gameHasStarted) {
        this.gameHasStarted = gameHasStarted;
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
    

    public VisibleCards getVisibleCards() { return visibleCards; }

    public void setVisibleCards(VisibleCards visibleCards) { this.visibleCards = visibleCards; }

}
