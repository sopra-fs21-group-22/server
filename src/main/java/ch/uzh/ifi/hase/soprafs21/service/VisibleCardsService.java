package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.VisibleCards;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.repository.VisibleCardsRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class VisibleCardsService {

    VisibleCardsRepository visibleCardsRepository;

    public VisibleCards createVisibleCards(){
        VisibleCards visibleCards = new VisibleCards();

        visibleCardsRepository.save(visibleCards);
        visibleCardsRepository.flush();

        return visibleCards;
    }

    public void pickACard(PlayerTable table, Player player, PlayCard cardToBePicked) {
        VisibleCards visibleCards = table.getVisibleCards();
        if(!visibleCards.getVisibleCards().isEmpty()){
            player.getHand().getPlayCards().add(cardToBePicked);
            visibleCards.removeACard(cardToBePicked);

            visibleCardsRepository.save(visibleCards);
            visibleCardsRepository.flush();
        } else {
            throw new UnsupportedOperationException("There is no card to be picked. Visible Cards is empty.");
        }
    }
}
