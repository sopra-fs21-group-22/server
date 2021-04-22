package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Entity
public class BrownCard extends PlayCard {

    @Override
    public String getColor() {
        return "brown";
    }

}