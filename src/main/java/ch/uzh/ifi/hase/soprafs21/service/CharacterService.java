package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.entity.CharacterPile;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.HandRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

@Service
@Transactional
public class CharacterService {

    @Autowired
    PlayerTableRepository playerTableRepository;

    
    public CharacterPile makeCharacterPile() {
        CharacterPile characterPile = new CharacterPile();
        List<CharacterCard> characterCards = new ArrayList<CharacterCard>();
        CharacterCard characterCard1 = new CharacterCard("Willy The Kid", 4);
        CharacterCard characterCard2 = new CharacterCard("Rose Doolan", 4);
        CharacterCard characterCard3 = new CharacterCard("Paul Regret", 3);
        CharacterCard characterCard4 = new CharacterCard("Jourdonnais", 4);
        CharacterCard characterCard5 = new CharacterCard("Bart Cassidy", 4);
        CharacterCard characterCard6 = new CharacterCard("Suzy Lafayette", 4);
        CharacterCard characterCard7 = new CharacterCard("El Gringo", 3);

        characterCards.add(characterCard1);
        characterCards.add(characterCard2);
        characterCards.add(characterCard3);
        characterCards.add(characterCard4);
        characterCards.add(characterCard5);
        characterCards.add(characterCard6);
        characterCards.add(characterCard7);

        characterPile.setCharacterCards(characterCards);
        return characterPile;

    }

    public CharacterCard pickCharacter(Player player, PlayerTable table) {
        Random rand = new Random();
        CharacterCard card = table.getCharacterPile().getCharacterCards()
                .get(rand.nextInt(table.getCharacterPile().getCharacterCards().size()));
        table.getCharacterPile().getCharacterCards()
                .remove(rand.nextInt(table.getCharacterPile().getCharacterCards().size()));
        player.setCharacterCard(card);
        player.setMaxBullets(player.getCharacterCard().getLifeAmount());
        player.setBullets(player.getCharacterCard().getLifeAmount());
        playerTableRepository.save(table);

        /* if (player.getCharacterCard().getName().equals("Willy The Kid")) {
            player.setPlayableBangsAnyRound(255);
            player.setStillPlayableBangsThisRound(255);
        }

        if (player.getCharacterCard().getName().equals("Rose Doolan")) {
            player.setBaseRange(2);
            player.setRange(2);
        }

        if (player.getCharacterCard().getName().equals("Paul Regret")) {
            player.setDistanceIncreaseForOthers(1);
        } */

        playerTableRepository.save(table);
        playerTableRepository.flush();
        return card;
    }
}
