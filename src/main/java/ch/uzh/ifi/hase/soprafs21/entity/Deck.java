package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.*;

import javax.persistence.*;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Entity
public class Deck {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "deck_id")
    private List<PlayCard> playCards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PlayCard> getPlayCards() {
        return playCards;
    }

    public void setPlayCards(List<PlayCard> playCards) {
        this.playCards = playCards;
    }

    public List<PlayCard> drawCards(int amount) {
        if (amount > this.playCards.size()) {
            // TODO add cards from discard pile
        }
        List<PlayCard> drawnCards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            drawnCards.add(this.playCards.remove(0));
        }
        return drawnCards;
    }

    public void addCard(PlayCard card) {
        if (this.playCards == null) {
            this.playCards = new ArrayList<>();
        }
        this.playCards.add(0, card);
    }
}