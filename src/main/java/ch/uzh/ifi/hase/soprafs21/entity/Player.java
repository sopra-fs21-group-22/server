package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.service.DeckService;

/**
 * The Player class represents a player The id is the same as the user who
 * "owns" the player has.
 */
@Entity
public class Player {

    @Id
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

    @OneToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Hand hand;

    @OneToOne
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

    @OneToOne
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

    public void takeHit() {
        this.bullets -= 1;
    }

    public void playCard(Long cardId, List<Player> targets) {
        PlayCard card = hand.getCardById(cardId);
        card.use(this, targets);
        hand.removeCard(card);

    }

    public boolean reachesWithWeapon(Player targetPlayer) {
        Player userRightNeighbor = this.getRightNeighbor();
        Player userLeftNeighbor = this.getLeftNeighbor();
        int rightDistance = 1;
        int leftDistance = 1;
        for (int i = 0; i < 7; i++) {
            if (userRightNeighbor.getId().equals(targetPlayer.getId())) {
                break;
            }
            rightDistance++;
            userRightNeighbor = userRightNeighbor.getRightNeighbor();
        }
        for (int i = 0; i < 7; i++) {
            if (userLeftNeighbor.getId().equals(targetPlayer.getId())) {
                break;
            }
            leftDistance++;
            userLeftNeighbor = userLeftNeighbor.getLeftNeighbor();
        }
        int distance = rightDistance < leftDistance ? rightDistance : leftDistance;

        return distance - this.getRange() + this.getDistanceDecreaseToOthers()
                + targetPlayer.getDistanceIncreaseForOthers() <= 0;

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

    public void setBullets(Integer bullets) {     
        this.bullets = bullets;
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

    public void pickACard(PlayCard card) {
        List<PlayCard> cards = new ArrayList<>();
        cards.add(card);
        hand.addCards(cards);
    }
}
