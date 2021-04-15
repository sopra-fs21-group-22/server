package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

import javax.persistence.*;
import java.util.List;

/**
 * This is the place cards are placed if they should be visible to all the players in
 * the game and then still used. For example the general store cards can be placed here.
 */

@Entity
public class VisibleCards {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "visibleCards_id")
    private List<PlayCard> visibleCards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<PlayCard> getVisibleCards() {
        return visibleCards;
    }

    public void setVisibleCards(List<PlayCard> visibleCards) {
        this.visibleCards = visibleCards;
    }

    public void removeACard(PlayCard cardToRemove){
        List<PlayCard> currVisibleCards = getVisibleCards();
        currVisibleCards.remove(cardToRemove);
        setVisibleCards(currVisibleCards);
    }

    public void addACard(PlayCard cardToAdd){
        List<PlayCard> currVisibleCards = getVisibleCards();
        currVisibleCards.add(cardToAdd);
        setVisibleCards(currVisibleCards);
    }

}
