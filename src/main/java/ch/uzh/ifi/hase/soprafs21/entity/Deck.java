package ch.uzh.ifi.hase.soprafs21.entity;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import ch.uzh.ifi.hase.soprafs21.entity.PlayCard;


@Entity
public class Deck {
    PlayCard CurrentCard = new OrangeCard();

    public PlayCard drawCard(){
        return CurrentCard;
   }

    public PlayCard discardCard(){
        return CurrentCard;
    }

    public void shuffle(){
        
    }
}