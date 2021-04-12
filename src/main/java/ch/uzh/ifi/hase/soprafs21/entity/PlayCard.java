package ch.uzh.ifi.hase.soprafs21.entity;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.cfg.NotYetImplementedException;

import java.util.*;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PlayCard {

    @Id
    @GeneratedValue
    private Long id;

    void use(Player usingPlayer, ArrayList<Player> targetPlayers) {
        throw new NotYetImplementedException();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    };
}
