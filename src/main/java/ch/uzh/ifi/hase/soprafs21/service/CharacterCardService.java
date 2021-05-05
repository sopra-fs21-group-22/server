package ch.uzh.ifi.hase.soprafs21.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.CharacterPile;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.repository.CharacterCardPileRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;

@Service
@Transactional
public class CharacterCardService {

    @Autowired
    CharacterCardPileRepository characterCardPileRepository;

    @Autowired
    PlayerTableRepository playerTableRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    VisibleCardsService visibleCardsService;
 
    public CharacterPile createCharacterPile() {
        CharacterPile characterPile = new CharacterPile();
        List<CharacterCard> characterCards = new ArrayList<CharacterCard>();
        /* CharacterCard characterCard1 = new CharacterCard("Willy The Kid", 4);
        CharacterCard characterCard2 = new CharacterCard("Rose Doolan", 4);
        CharacterCard characterCard3 = new CharacterCard("Paul Regret", 3);
        CharacterCard characterCard4 = new CharacterCard("Jourdonnais", 4);
        CharacterCard characterCard5 = new CharacterCard("Bart Cassidy", 4);
        CharacterCard characterCard6 = new CharacterCard("Suzy Lafayette", 4);
        CharacterCard characterCard7 = new CharacterCard("El Gringo", 3); */

        CharacterCard characterCard1 = new CharacterCard();
        CharacterCard characterCard2 = new CharacterCard();
        CharacterCard characterCard3 = new CharacterCard();
        CharacterCard characterCard4 = new CharacterCard();
        CharacterCard characterCard5 = new CharacterCard();
        CharacterCard characterCard6 = new CharacterCard();
        CharacterCard characterCard7 = new CharacterCard();
        characterCard1.setLifeAmount(4);
        characterCard1.setName("Willy The Kid");
        characterCard2.setLifeAmount(4);
        characterCard2.setName("Rose Doolan");
        characterCard3.setLifeAmount(3);
        characterCard3.setName("Paul Regret");
        characterCard4.setLifeAmount(4);
        characterCard4.setName("Jourdonnais");
        characterCard5.setLifeAmount(4);
        characterCard5.setName("Bart Cassidy");
        characterCard6.setLifeAmount(4);
        characterCard6.setName("Suzy Lafayette");
        characterCard7.setLifeAmount(3);
        characterCard7.setName("El Gringo");

        characterCards.add(characterCard1);
        characterCards.add(characterCard2);
        characterCards.add(characterCard3);
        characterCards.add(characterCard4);
        characterCards.add(characterCard5);
        characterCards.add(characterCard6);
        characterCards.add(characterCard7);

        characterPile.setCharacterCards(characterCards);
               
        characterCardPileRepository.save(characterPile);
        characterCardPileRepository.flush();
        return characterPile;

    }

    public CharacterCard pickCharacter(Player player, PlayerTable table) {
        List<CharacterCard> characterCards = table.getCharacterPile().getCharacterCards();
        //Collections.shuffle(characterCards);      //randomizes character cards
        CharacterCard card = characterCards.get(0);
        //characterCards.remove(0); // removes already chosen card
        player.setCharacterCard(card);
        //player.setMaxBullets(player.getCharacterCard().getLifeAmount());
        //player.setBullets(player.getCharacterCard().getLifeAmount());
        //table.getCharacterPile().setCharacterCards(characterCards); // change the cardPile
        playerTableRepository.save(table);

         /* if (player.getCharacterCard().getName().equals("Willy The Kid")) {
            player.setPlayableBangsAnyRound(255);
            player.setStillPlayableBangsThisRound(255);
        }

        if (player.getCharacterCard().getName().equals("Rose Doolan")) {        //special abilities
            player.setBaseRange(2);
            player.setRange(2);
        }

        if (player.getCharacterCard().getName().equals("Paul Regret")) {
            player.setDistanceIncreaseForOthers(1);
        }  */

        playerTableRepository.save(table);
        
        return card;
    }
}
