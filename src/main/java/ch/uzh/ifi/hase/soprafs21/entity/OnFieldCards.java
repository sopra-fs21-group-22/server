package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.util.List;

public class OnFieldCards {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PlayCard> onFieldCards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLength(){
        List<PlayCard> temp = onFieldCards;
        return temp.size();
    }

    public List<PlayCard> getOnFieldCards() {
        return onFieldCards;
    }

    public void setOnFieldCards(List<PlayCard> cards) {
        for (int i = 0; i < cards.size(); i++) {
            onFieldCards.add(cards.get(0));
        }
    }

    public void addOnFieldCard(BlueCard card){
        onFieldCards.add(card);
    }

    public void removeOnFieldCard(BlueCard card) {
        onFieldCards.remove(card);
    }

}
