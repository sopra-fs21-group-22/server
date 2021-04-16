package ch.uzh.ifi.hase.soprafs21.rest.dto.game;

import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;

public class PlayCardGetDTO {
    protected Long id;
    protected Suit suit;
    protected Rank rank;
    protected String color;
    protected String name;

    public String getName() {
        return "HIDDEN";
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Suit getSuit() {
        return Suit.HIDDEN;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Rank getRank() {
        return Rank.HIDDEN;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
