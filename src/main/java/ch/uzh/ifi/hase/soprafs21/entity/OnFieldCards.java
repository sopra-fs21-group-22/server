package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.util.List;

public class OnFieldCards {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PlayCard> onFieldCards;

    public List<PlayCard> getOnFieldCards() {
        return onFieldCards;
    }

    public void addCard(BlueCard card, Player player){
        onFieldCards.add(card);
    }

    public void changeOwner(BlueCard card, Player prevOwner, Player newOwner) {

    }

}
