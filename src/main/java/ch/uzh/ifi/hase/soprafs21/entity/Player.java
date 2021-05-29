package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import ch.uzh.ifi.hase.soprafs21.entity.gameMoves.GameMove;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PayLoadDTO;

/**
 * The Player class represents a player The id is the same as the user who
 * "owns" the player has.
 */
@Entity
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Integer maxBullets = 4;

    @Column
    private Integer bullets = 4;

    @Column
    private Integer range = 1;

    @Column
    private Integer baseRange = 1;

    @Column
    private Integer distanceIncreaseForOthers = 0;

    @Column
    Integer distanceDecreaseToOthers = 0;

    @Column
    Integer stillPlayableBangsThisRound = 1;

    @Column
    Integer playableBangsAnyRound = 1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    private Hand hand;

    @OneToOne(cascade = CascadeType.ALL)
    private CharacterCard characterCard;

    @Column
    private GameRole gameRole = GameRole.HIDDEN;

    @OneToOne
    private Player leftNeighbor;

    @OneToOne
    private Player rightNeighbor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playertable_id", nullable = false)
    private PlayerTable table;

    @Column
    private Boolean ready = false;

    @Column
    private Integer strikes = 0;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    private OnFieldCards onFieldCards;

    public OnFieldCards getOnFieldCards() {
        if (onFieldCards == null) {
            this.onFieldCards = new OnFieldCards();
            return onFieldCards;
        }
        return onFieldCards;
    }

    public void setOnFieldCards(OnFieldCards cards) {
        this.onFieldCards = cards;
    }

    public void takeHit(Player attacker, PlayCard card) {
        boolean isSafe = false;

        // see if character equals Jourdonnais
        // if (this.getCharacterCard().getName().equals("Jourdonnais")) {
        // PlayCard card = this.getTable().getDeck().drawCards(1).get(0); // Jourdonnais
        // Ability
        // this.getTable().getDiscardPile().addCard(card);
        // isSafe = card.getSuit().equals(Suit.HEARTS);

        // }

        // first go through onFieldCards for the Barrel
        if (!isSafe) {
            for (int i = 0; i < onFieldCards.getLength(); i++) {
                isSafe = onFieldCards.get(i).onBang(this);
                if (isSafe) {
                    break;

                }
            }
        }
        // then go through Hand cards for Missed & Beer
        int i = 0;
        while (!isSafe && i < hand.getLength()) {
            if (hand.get(i).getColor().equals("brown")) { // to make sure no blue cards on hand are activated
                isSafe = hand.get(i).onBang(this); // since hand is in order of priority this will check Missed before
                                                   // Beer
            }
            i++;
        }

        if (!isSafe) {
            takeUnblockableHit(attacker, card);
            if (this.bullets > 0) {
                // TODO use instead something like this

                // List<PlayCard> cards = table.getDeck().drawCards(amount);
                // hand.addCards(cards);

                // if (this.getCharacterCard().getName().equals("Bart Cassidy")){ // Bart
                // Cassidy Ability

                // DeckService deckservice = new DeckService();
                // deckservice.drawCards(this.getTable(), this, 1);
                // }
                // if (this.getCharacterCard().getName().equals("El Gringo")){ // El Gringo
                // Ability
                // DeckService deckservice = new DeckService();
                // deckservice.gringoDraw(this, attacker);
                // }
            }
        }
    }

    public void takeUnblockableHit(Player attacker, PlayCard card) {
        String message = String.format("%s got hit and lost a bullet!", user.getUsername());
        GameMove gameMove = new GameMove(attacker, this, card, GameMoveAction.HOLED, message);
        table.addGameMove(gameMove);
        bullets--;
        if (bullets == 0) {
            onDeath(attacker);
        }
    }

    public Player getAliveRightNeighbor() {
        Player nextPlayer = rightNeighbor;
        while (nextPlayer.getBullets() == 0 && !nextPlayer.getId().equals(id)) {
            nextPlayer = nextPlayer.getRightNeighbor();
        }
        return nextPlayer;
    }

    /**
     * Handles death by another player
     * 
     * @param killer
     */
    public void onDeath(Player killer) {
        String consequenceMessage = "";
        if (killer.getGameRole().equals(GameRole.SHERIFF) && this.getGameRole().equals(GameRole.DEPUTY)) {
            // punish sheriff
            consequenceMessage = String.format(" %s has to discard all his cards!", killer.getUser().getUsername());
            List<PlayCard> handCards = killer.getHand().getPlayCards();
            List<BlueCard> killerOnFieldCards = killer.getOnFieldCards().getOnFieldCards();
            for (PlayCard card : handCards) {
                killer.getTable().getDiscardPile().addCard(card);
            }
            for (PlayCard card : killerOnFieldCards) {
                killer.getTable().getDiscardPile().addCard(card);
            }
            killer.getHand().setPlayCards(new ArrayList<>());
            killer.getOnFieldCards().removeAllCards();

        } else if (this.getGameRole().equals(GameRole.OUTLAW)) {
            // killer draws three cards
            consequenceMessage = String.format(" The %s can draw three cards!", killer.getUser().getUsername());
            killer.getHand().addCards(killer.getTable().getDeck().drawCards(3));
        }

        String message = String.format("%s killed %s %s!%s", killer.getUser().getUsername(), gameRole,
                user.getUsername(), consequenceMessage);
        GameMove gameMove = new GameMove(killer, this, null, GameMoveAction.KILL, message);
        table.addGameMove(gameMove);

        onDeath();
    }

    /**
     * Handles death not caused by a player
     */
    public void onDeath() {
        String message = String.format("%s died!", user.getUsername());
        GameMove gameMove = new GameMove(this, null, null, GameMoveAction.DEATH, message);
        table.addGameMove(gameMove);

        Deck discardPile = table.getDiscardPile();
        // remove hand cards
        discardPile.addCards(hand.getPlayCards());
        hand.removeAllCards();
        // remove onFieldCards
        for (PlayCard card : onFieldCards.getOnFieldCards()) {
            discardPile.addCard(card);
        }

        onFieldCards.removeAllCards();
        determineWinner();
        if (table.getPlayerOnTurn().getId().equals(this.id)) {
            Player nextPlayer = rightNeighbor;
            while (nextPlayer.getBullets() == 0 && !nextPlayer.getId().equals(id)) {
                nextPlayer = nextPlayer.getRightNeighbor();

            }
            table.setPlayerOnTurn(nextPlayer);
        }
    }

    private void determineWinner() {
        List<GameRole> gameRoles = new ArrayList<>();
        List<Player> players = table.getAlivePlayers();
        String winnerMessage = "";

        for (Player player : players) {
            gameRoles.add(player.getGameRole());
        }

        Boolean sheriffAlive = gameRoles.contains(GameRole.SHERIFF);
        Boolean outlawAlive = gameRoles.contains(GameRole.OUTLAW);
        Boolean renegadeAlive = gameRoles.contains(GameRole.RENEGADE);
        Boolean deputyAlive = gameRoles.contains(GameRole.DEPUTY);

        // game is not over
        if (sheriffAlive && (outlawAlive || renegadeAlive)) {
            return;
        }
        if (sheriffAlive) {
            if (table.getPlayers().size() == 4) {
                winnerMessage = "Order has been restored!\nThe SHERIFF won the game!";
            } else if (table.getPlayers().size() < 7) {
                winnerMessage = "Order has been restored!\nThe SHERIFF and DEPUTY won the game!";
            } else if (table.getPlayers().size() == 7){
                winnerMessage = "Order has been restored!\nThe SHERIFF and DEPUTYs won the game!";
            }
        } else if (outlawAlive || deputyAlive) {
            winnerMessage = "The Wild West will remain wild!\nThe OUTLAWs won the game!";
        } else if (renegadeAlive) {
            winnerMessage = "Last player standing!\nThe RENEGADE won the game!";
        }

        GameMove gameMove = new GameMove(null, null, null, GameMoveAction.WIN, winnerMessage);
        this.table.addGameMove(gameMove);

        this.table.setGameStatus(GameStatus.ENDED);
    }

    public void playCard(Long cardId, Player target, PayLoadDTO payload) {
        PlayCard card = hand.getCardById(cardId);
        card.use(this, target, payload);
        hand.removeCard(card);
    }

    public boolean reachesWithDistance(Player target, int distance) {
        return this.getDistanceToNeighbor(target) - distance - this.getDistanceDecreaseToOthers()
                + target.getDistanceIncreaseForOthers() <= 0;
    }

    public boolean reachesWithWeapon(Player targetPlayer) {
        return this.getDistanceToNeighbor(targetPlayer) - this.getRange() - this.getDistanceDecreaseToOthers()
                + targetPlayer.getDistanceIncreaseForOthers() <= 0;
    }

    /** returns distance while ignoring all modifiers */
    private int getDistanceToNeighbor(Player targetPlayer) {
        Player userRightNeighbor = this.getRightNeighbor();
        Player userLeftNeighbor = this.getLeftNeighbor();
        int rightDistance = 1;
        int leftDistance = 1;

        for (int i = 0; i < 7; i++) {
            if (userRightNeighbor.getId().equals(targetPlayer.getId())) {
                break;
            }
            if (userRightNeighbor.getBullets() > 0) {
                rightDistance++;
            }

            userRightNeighbor = userRightNeighbor.getRightNeighbor();
        }
        for (int i = 0; i < 7; i++) {
            if (userLeftNeighbor.getId().equals(targetPlayer.getId())) {
                break;
            }
            if (userLeftNeighbor.getBullets() > 0) {
                leftDistance++;
            }
            userLeftNeighbor = userLeftNeighbor.getLeftNeighbor();
        }
        return rightDistance < leftDistance ? rightDistance : leftDistance;
    }

    public PlayerTable getTable() {
        return table;
    }

    public void setTable(PlayerTable table) {
        this.table = table;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public Integer getBaseRange() {
        return baseRange;
    }

    public void setBaseRange(Integer baseRange) {
        this.baseRange = baseRange;
    }

    public Integer getBullets() {
        return bullets;
    }

    public void setBullets(Integer newBullets) {
        if (newBullets > maxBullets) {
            return;
        }
        this.bullets = newBullets;
        if (newBullets == 0) {
            this.onDeath();
        }
    }

    public Integer getMaxBullets() {
        return maxBullets;
    }

    public void setMaxBullets(Integer maxBullets) {
        this.maxBullets = maxBullets;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public CharacterCard getCharacterCard() {
        return characterCard;
    }

    public void setCharacterCard(CharacterCard characterCard) {
        this.characterCard = characterCard;
    }

    public GameRole getGameRole() {
        return gameRole;
    }

    public void setGameRole(GameRole gameRole) {
        this.gameRole = gameRole;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        final Player other = (Player) obj;

        return other.getId().equals(this.getId());
    }

    public Integer getDistanceIncreaseForOthers() {
        return distanceIncreaseForOthers;
    }

    public void setDistanceIncreaseForOthers(Integer distanceIncreaseForOthers) {
        this.distanceIncreaseForOthers = distanceIncreaseForOthers;
    }

    public Integer getDistanceDecreaseToOthers() {
        return distanceDecreaseToOthers;
    }

    public void setDistanceDecreaseToOthers(Integer distanceDecreaseToOthers) {
        this.distanceDecreaseToOthers = distanceDecreaseToOthers;
    }

    public Player getLeftNeighbor() {
        return leftNeighbor;
    }

    public void setLeftNeighbor(Player leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    public Player getRightNeighbor() {
        return rightNeighbor;
    }

    public void setRightNeighbor(Player rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }

    public Integer getStillPlayableBangsThisRound() {
        return stillPlayableBangsThisRound;
    }

    public void setStillPlayableBangsThisRound(Integer stillPlayableBangsThisRound) {
        this.stillPlayableBangsThisRound = stillPlayableBangsThisRound;
    }

    public Integer getPlaybleBangsAnyRound() {
        return playableBangsAnyRound;
    }

    public void setPlayableBangsAnyRound(Integer playableBangsAnyRound) {
        this.playableBangsAnyRound = playableBangsAnyRound;
    }

    public Integer getStrikes() {
        return strikes;
    }

    public void setStrikes(Integer strikes) {
        this.strikes = strikes;
    }
}
