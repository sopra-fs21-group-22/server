package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

import org.hibernate.mapping.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.*;

import javax.transaction.Transactional;

@SpringBootTest
public class DeckServiceTest {
    
    @Autowired
    DeckService deckService;

    @Autowired
    DeckRepository deckRepository;

    @AfterEach
    public void afterEach() {
        deckRepository.deleteAll();
        deckRepository.flush();
    }

    @Transactional
    @Test
    public void deckCreationSuccess() {

        
        Deck deck = new Deck();

        deckService.fill(deck);

        ArrayList<PlayCard> testList = (ArrayList<PlayCard>) deck.getPlayCards();

        for(int i = 0; i < testList.size(); i++) {   
            System.out.print(testList.get(i));
        }  

        
    }
    @Transactional
    @Test
    public void deckShuffleSuccess() {

        
        Deck deck = new Deck();
        Deck discardPile = new Deck();


        deckService.fill(discardPile);

        ArrayList<PlayCard> testList = (ArrayList<PlayCard>) discardPile.getPlayCards();

        for(int i = 0; i < testList.size(); i++) {   
            System.out.print(testList.get(i));
        }  

        deckService.shuffle(deck, discardPile);
        
        ArrayList<PlayCard> testList2 = (ArrayList<PlayCard>) discardPile.getPlayCards();

        for(int i = 0; i < testList2.size(); i++) {   
            System.out.print(testList2.get(i));
        }

        ArrayList<PlayCard> testList3 = (ArrayList<PlayCard>) deck.getPlayCards();

        for(int i = 0; i < testList3.size(); i++) {   
            System.out.print(testList3.get(i));
        }
    }
}
