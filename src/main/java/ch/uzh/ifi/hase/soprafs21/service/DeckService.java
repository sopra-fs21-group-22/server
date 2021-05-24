package ch.uzh.ifi.hase.soprafs21.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.repository.DeckRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Service
@Transactional
public class DeckService {

    @Autowired
    DeckRepository deckRepository;

    @Autowired
    PlayerTableRepository playerTableRepository;

    @Autowired
    PlayerRepository playerRepository;

    public void fill(Deck deck) {

        List<PlayCard> playCards = PlayCardService.getPlayCards();

        deck.setPlayCards(playCards);

        deckRepository.save(deck);
        deckRepository.flush();

    }

    public Deck createDeck() {
        Deck deck = new Deck();
        this.fill(deck);
        deckRepository.save(deck);
        deckRepository.flush();

        return deck;
    }

    public Deck createDiscardPile() {
        Deck deck = new Deck();
        List<PlayCard> playCards = new ArrayList<PlayCard>();

        deck.setPlayCards(playCards);
        deckRepository.save(deck);
        deckRepository.flush();

        return deck;
    }

    public void shuffle(PlayerTable table) {
        List<PlayCard> discardCards = table.getDiscardPile().getPlayCards();

        List<PlayCard> toBeAddedCards = new ArrayList<>(discardCards);

        List<PlayCard> topCard = new ArrayList<>(discardCards.subList(0, 1));

        Collections.shuffle(toBeAddedCards);

        table.getDiscardPile().setPlayCards(topCard);
        table.getDeck().setPlayCards(toBeAddedCards);

    }

    public void drawCards(PlayerTable table, Player player, Integer n) {
        for (int i = 0; i < n; i++) {
            if (table.getDeck().getPlayCards().size() < 1) {
                this.shuffle(table);
            }
            player.getHand().addCardInOrder(table.getDeck().getPlayCards().get(0));

            table.getDeck().getPlayCards().remove(0);
        }
        playerTableRepository.save(table);
        playerTableRepository.flush();
    }

    public void gringoDraw(Player player, Player attacker) {

        PlayerTable table = player.getTable();

        if (attacker.getHand().getPlayCards().size() > 0) {
            PlayCard playCard = attacker.getHand().getPlayCards().get(0);
            attacker.getHand().getPlayCards().remove(0);
            player.getHand().addCardInOrder(playCard);
            if (attacker.getHand().getPlayCards().size() < 1) {
                if (attacker.getCharacterCard().getName().equals("Suzy Lafayette")) { // Lafayette Ability
                    this.drawCards(table, attacker, 1);
                }
            }

            playerTableRepository.save(table);
            playerTableRepository.flush();
        }
    }
}
