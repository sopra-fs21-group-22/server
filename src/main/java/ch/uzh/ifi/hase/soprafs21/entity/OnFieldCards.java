package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
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
        return temp == null ? 0 : temp.size();
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
        Priority cardPrio = card.getPriority();
        int index = (cards == null || cards.size() == 0) ? 0 : cards.size() - 1;

        int i = 0;
        if(cardPrio == Priority.FIRST || index == 0){
            index = 0;
        } else if (cardPrio == Priority.SECOND){
            while (cards.get(i).getPriority() == Priority.FIRST){ // in case there are multiple cards with the Priority FIRST
                i++;
            }
            index = i;
        } else if (cardPrio == Priority.THIRD){
            while (cards.get(i).getPriority() == Priority.FIRST || cards.get(index).getPriority() == Priority.SECOND){ // in case there are multiple cards with the Priority FIRST/SECOND
                i++;
            }
            index = i;
        }
        cards.add(index, card);
    }

    public Boolean contains(BlueCard card){
        for (BlueCard blueCard : cards) {
            if (blueCard.getCard() == card.getCard()) {
                return true;
            }
        }
        return false;
    }

    public void removeOnFieldCard(BlueCard card) {
        cards.remove(card);
    }

    // TODO check for duplicates (player playing a weapon card when one is already
    // present)
    // probably best to run card.undo() for old card and card.use() for new card
    // (with function arguments obviously)

}
