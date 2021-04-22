package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.*;

import javax.persistence.*;

import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;

@Entity
public class CharacterPile {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "characterpile_id")
    private List<CharacterCard> characterCards;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    public void setCharacterCards(List<CharacterCard> characterCards) {
        this.characterCards = characterCards;
    }
}