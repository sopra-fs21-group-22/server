package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;

@Entity
public abstract class BlueCard extends PlayCard {

    @Transient
    @Autowired
    PlayerRepository playerRepository;

    @GeneratedValue
    @Id
    private Long id;

    public abstract void undo(Player affectedPlayer);
}