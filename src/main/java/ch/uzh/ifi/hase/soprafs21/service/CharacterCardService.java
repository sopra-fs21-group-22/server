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

    public CharacterPile createCharacterPile() {
        CharacterPile characterPile = new CharacterPile();
        List<CharacterCard> characterCards = new ArrayList<CharacterCard>();
        /*
         * CharacterCard characterCard1 = new CharacterCard("Willy The Kid", 4);
         * CharacterCard characterCard2 = new CharacterCard("Rose Doolan", 4);
         * CharacterCard characterCard3 = new CharacterCard("Paul Regret", 3);
         * CharacterCard characterCard4 = new CharacterCard("Jourdonnais", 4); // for
         * use with buggy constructor version of charactercard class CharacterCard
         * characterCard5 = new CharacterCard("Bart Cassidy", 4); // do not enable
         * CharacterCard characterCard6 = new CharacterCard("Suzy Lafayette", 4);
         * CharacterCard characterCard7 = new CharacterCard("El Gringo", 3);
         */

        CharacterCard characterCard1 = new CharacterCard();
        CharacterCard characterCard2 = new CharacterCard();
        CharacterCard characterCard3 = new CharacterCard();
        CharacterCard characterCard4 = new CharacterCard();
        CharacterCard characterCard5 = new CharacterCard();
        CharacterCard characterCard6 = new CharacterCard();
        CharacterCard characterCard7 = new CharacterCard();
        characterCard1.setLifeAmount(4);
        characterCard1.setName("willythekid");
        characterCard1.setDisplay("Willy The Kid");
        characterCard1.setDescription("You can use unlimited BANG! cards per round. You start with 4 Bullets.");
        characterCard2.setLifeAmount(4);
        characterCard2.setName("rosedoolan");
        characterCard2.setDisplay("Rose Doolan");
        characterCard2.setDescription("Your range is permanently increased by one. You start with 4 Bullets.");
        characterCard3.setLifeAmount(3);
        characterCard3.setName("paulregret");
        characterCard3.setDisplay("Paul Regret");
        characterCard3.setDescription("The range others need to hit you is permanently increased by one. You start with 3 Bullets.");
        characterCard4.setLifeAmount(4);
        characterCard4.setName("jourdonnais");
        characterCard4.setDisplay("Jourdonnais");
        characterCard4.setDescription("You can use unlimited BANG! cards per round. You start with 4 Bullets.");
        characterCard5.setLifeAmount(4);
        characterCard5.setName("bartcassidy");
        characterCard5.setDisplay("Bart Cassidy");
        characterCard5.setDescription("You can use unlimited BANG! cards per round. You start with 4 Bullets.");
        characterCard6.setLifeAmount(4);
        characterCard6.setName("suzylafayette");
        characterCard6.setDisplay("Suzy Lafayette");
        characterCard6.setDescription("Your range is permanently increased by one. You start with 4 Bullets.");
        characterCard7.setLifeAmount(3);
        characterCard7.setName("elgringo");
        characterCard7.setDisplay("El Gringo");
        characterCard7.setDescription("The range others need to hit you is permanently increased by one. You start with 3 Bullets.");

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
        Collections.shuffle(characterCards); // randomizes character cards
        CharacterCard card = characterCards.get(0);
        characterCards.remove(0); // removes already chosen card
        player.setCharacterCard(card);
        player.setMaxBullets(player.getCharacterCard().getLifeAmount());
        player.setBullets(player.getCharacterCard().getLifeAmount());
        table.getCharacterPile().setCharacterCards(characterCards);
        playerTableRepository.save(table);

        if (player.getCharacterCard().getDisplay().equals("Willy The Kid")) {
            player.setPlayableBangsAnyRound(255);
            player.setStillPlayableBangsThisRound(255);
        }

        if (player.getCharacterCard().getDisplay().equals("Rose Doolan")) { // special abilities
            player.setBaseRange(2);
            player.setRange(2);
        }

        if (player.getCharacterCard().getDisplay().equals("Paul Regret")) {
            player.setDistanceIncreaseForOthers(1);
        }

        if (player.getCharacterCard().getDisplay().equals("Jourdonnais")) {
            player.setPlayableBangsAnyRound(255);
            player.setStillPlayableBangsThisRound(255);
        }

        if (player.getCharacterCard().getDisplay().equals("Suzy Lafayette")) { 
            player.setBaseRange(2);
            player.setRange(2);
        }

        if (player.getCharacterCard().getDisplay().equals("El Gringo")) {
            player.setDistanceIncreaseForOthers(1);
        }

        if (player.getCharacterCard().getDisplay().equals("Bart Cassidy")) {
            player.setPlayableBangsAnyRound(255);
            player.setStillPlayableBangsThisRound(255);
        }

        playerTableRepository.save(table);

        return card;
    }
}
