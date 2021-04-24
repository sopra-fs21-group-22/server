package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.Priority;
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

    public BlueCard get(int index){
        return cards.get(index);
    }

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

    public List<BlueCard> getOnFieldCards() {
        return cards;
    }

    public void setOnFieldCards(List<BlueCard> cards) {
        for (BlueCard card : cards) {
            addOnFieldCard(card); // makes sure the cards are added in order
        }
    }

    /**
     * This function makes sure that the cards in OnFieldCards are added in the order of their priority.
     * The first card (index 0) has the highest priority and the last card the lowest. The cards with
     * the same priority are in arbitrary order.
     */

    public void addOnFieldCard(BlueCard card){
        // TODO depending on how many priorities there will be --> loop over priorities instead of if else
        if(card.getPriority() == Priority.FIRST){
            cards.add(0, card);
        } else if (card.getPriority() == Priority.SECOND){
            int i = 0;
            while (cards.get(i).getPriority() == Priority.FIRST){ // in case there are multiple cards with the Priority FIRST
                i++;
            }
            cards.add(i, card);
        } else if (card.getPriority() == Priority.THIRD){
            int i = 0;
            while (cards.get(i).getPriority() == Priority.FIRST || cards.get(i).getPriority() == Priority.SECOND){ // in case there are multiple cards with the Priority FIRST/SECOND
                i++;
            }
            cards.add(i, card);
        } else {
            cards.add(card);
        }
    }

    public void removeOnFieldCard(BlueCard card) {
        cards.remove(card);
    }

    // TODO check for duplicates (player playing a weapon card when one is already
    // present)
    // probably best to run card.undo() for old card and card.use() for new card
    // (with function arguments obviously)

}
