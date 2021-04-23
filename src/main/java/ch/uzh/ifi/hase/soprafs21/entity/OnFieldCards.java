package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class OnFieldCards {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "onFieldCards_id")
    private List<BlueCard> cards = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLength() {
        List<BlueCard> temp = cards;
        return temp.size();
    }

    public boolean isInJail() {
        for (BlueCard card : cards) {
            if (card.getCard() == Card.JAIL) {
                return true;
            }
        }
        return false;
    }

    public void removeJailCard() {
        for (BlueCard card : cards) {
            if (card.getCard() == Card.JAIL) {
                removeOnFieldCard(card);
                // TODO: add card to discard pile
            }
        }
    }

    public boolean hasDynamite() {
        for (BlueCard card : cards) {
            if (card.getCard() == Card.DYNAMITE) {
                return true;
            }
        }
        return false;
    }

    public void removeDynamiteCard() {
        for (BlueCard card : cards) {
            if (card.getCard() == Card.DYNAMITE) {
                removeOnFieldCard(card);
                // TODO put on stack
            }
        }
    }

    public void moveDynamiteCardToTheLeft(Player currPlayer) {
        for (BlueCard card : cards) {
            if (card.getCard() == Card.DYNAMITE) {
                removeOnFieldCard(card);
                currPlayer.getLeftNeighbor().getOnFieldCards().addOnFieldCard(card);
            }
        }
    }

    public List<BlueCard> getOnFieldCards() {
        return cards;
    }

    public void setOnFieldCards(List<BlueCard> cards) {
        for (int i = 0; i < cards.size(); i++) {
            cards.add(cards.get(0));
        }
    }

    public void addOnFieldCard(BlueCard card) {
        cards.add(card);
    }

    public void removeOnFieldCard(BlueCard card) {
        cards.remove(card);
    }

    // TODO check for duplicates (player playing a weapon card when one is already
    // present)
    // probably best to run card.undo() for old card and card.use() for new card
    // (with function arguments obviously)

}
