package ch.uzh.ifi.hase.soprafs21.entity.cards.blueCards;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.Priority;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.entity.Deck;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.service.DeckService;

import javax.persistence.Entity;

@Entity
public class Dynamite extends BlueCard {
    public Dynamite() {
    }


    public Dynamite(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.card = Card.DYNAMITE;
        this.priority = Priority.FIRST;
    }

    @Override
    protected void onPlacement(Player usingPlayer, Player randomPlayer) {
        usingPlayer.getOnFieldCards().addOnFieldCard(this); // the dynamite card is always placed on your own onFieldCards as soon as you have it in your hands
    }

    @Override
    public void onTurnStart(Player affectedPlayer) {
        PlayerTable table = affectedPlayer.getTable();
        Deck deck = table.getDeck();
        PlayCard referenceCard = deck.drawCards(1).get(0);
        System.out.println("SUIT: " + referenceCard.getSuit());
        Rank r = referenceCard.getRank();
        Boolean rankBetweenTwoAndNine = (r != Rank.TEN && r != Rank.JACK && r != Rank.QUEEN && r != Rank.KING && r != Rank.ACE);
        if(referenceCard.getSuit() == Suit.SPADES && rankBetweenTwoAndNine){ // explosion
            int lives = affectedPlayer.getBullets();
            if (lives > 3) {
                affectedPlayer.setBullets(lives - 3);

                if (affectedPlayer.getCharacterCard().getName().equals("Bart Cassidy")){          // Bart Cassidy Ability
                    DeckService deckservice = new DeckService();
                    deckservice.cassidyDraw(affectedPlayer); 
                } 

                affectedPlayer.getOnFieldCards().removeOnFieldCard(this);
            } else{
                affectedPlayer.getOnFieldCards().removeOnFieldCard(this);
                // player dies
                // TODO handle death
            }
        } else { // no explosion
            affectedPlayer.getOnFieldCards().removeOnFieldCard(this);
            affectedPlayer.getLeftNeighbor().getOnFieldCards().addOnFieldCard(this); // move Dynamite card to the left
        }
    }

    @Override
    public void onRemoval(Player affectedPlayer) {
        // nothing happens
    }

    @Override
    protected boolean targetIsValid(Player usingPlayer, Player targetPlayer) {
        return usingPlayer.getId().equals(targetPlayer.getId());
    }
}
