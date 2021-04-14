package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

import org.hibernate.mapping.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.*;

import javax.transaction.Transactional;

public class DeckServiceTest {

    // @Autowired
    // DeckService deckService;

    @Transactional
    @Test
    public void deckCreationSuccess() {

        DeckService deckService = new DeckService();
        Deck deck = new Deck();

        deckService.fill(deck);

        ArrayList<PlayCard> testList = (ArrayList<PlayCard>) deck.getPlayCards();

        for (int i = 0; i < testList.size(); i++) {
            System.out.print(testList.get(i));
        }

    }
}
