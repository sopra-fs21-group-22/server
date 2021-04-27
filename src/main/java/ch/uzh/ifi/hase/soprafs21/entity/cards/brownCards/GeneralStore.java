package ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards;

import java.util.List;

import javax.persistence.Entity;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.VisibleCards;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;

@Entity
public class GeneralStore extends BrownCard {
    public GeneralStore() {
    }

    public GeneralStore(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.GENERALSTORE;
    }

    @Override
    protected void onPlacement(Player usingPlayer, List<Player> targets) {
        int number_of_players = targets.size();
        PlayerTable table = usingPlayer.getTable();
        Deck currDeck = table.getDeck();

        // draw cards from deck and add them to visible cards
        List<PlayCard> drawnCards = currDeck.drawCards(number_of_players);
        VisibleCards currVisibleCards = table.getVisibleCards();
        currVisibleCards.setVisibleCards(drawnCards);

        /*
         * Now each player chooses a card --> happens in the frontend, once every player
         * has chosen a card, we get a list of players and their chosen cards which are
         * then added to their hand and removed from the visible cards. This all happens
         * in the game controller. The frontend has to make a get visible cards request
         * once a General Store is played, and collect all cards picked by each player
         * in one List and then make a post mapping which then handles the logic.
         */
    }

}
