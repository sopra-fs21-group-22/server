package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class OnFieldCards {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "onFieldCards_id")
    private List<BlueCard> onFieldCards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLength(){
        List<BlueCard> temp = onFieldCards;
        return temp.size();
    }

    public List<BlueCard> getOnFieldCards() {
        return onFieldCards;
    }

    public void setOnFieldCards(List<BlueCard> cards) {
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
