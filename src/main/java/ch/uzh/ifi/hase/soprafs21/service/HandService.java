package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.repository.HandRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards.BlueCard;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;

@Service
@Transactional
public class HandService {

    @Autowired
    HandRepository handRepository;

    @Autowired
    PlayerTableRepository playerTableRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    SpecificCardService specificCardService;

    public Hand createHand() {
        Hand hand = new Hand();
        List<PlayCard> playCards = new ArrayList<PlayCard>();

        hand.setPlayCards(playCards);
        handRepository.save(hand);
        handRepository.flush();
        ;

        return hand;
    }

    public void layCard(PlayerTable table, Player user, List<Player> targets, Long cardId) {
        PlayCard card = user.getHand().getCardById(cardId);
        specificCardService.use(table, card, user, targets);
        if (card.getColor().equals("brown")) {
            table.getDiscardPile().getPlayCards().add(0, card);

        } else {
            if (targets.size() != 1) {
                throw new GameLogicException("Blue cards can only be played on one player!");
            }
            Player target = targets.get(0);
            target.getOnFieldCards().addOnFieldCard((BlueCard) card);
        }
        user.getHand().getPlayCards().remove(card);

        playerTableRepository.saveAndFlush(table);
        playerRepository.saveAndFlush(user);
    }

}
