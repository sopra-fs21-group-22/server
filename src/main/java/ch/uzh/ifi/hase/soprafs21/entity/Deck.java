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
    PlayCard PlayCard = new PlayCard();

    public PlayCard drawCard(){
        
   }

    public PlayCard discardCard(){

    }

    public void shuffle(){
        
    }
}