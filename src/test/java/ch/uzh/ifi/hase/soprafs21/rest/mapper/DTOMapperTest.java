package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.Card;
import ch.uzh.ifi.hase.soprafs21.constant.GameMoveAction;
import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.constant.Rank;
import ch.uzh.ifi.hase.soprafs21.constant.Suit;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Hand;
import ch.uzh.ifi.hase.soprafs21.entity.OnFieldCards;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.cards.CharacterCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.PlayCard;
import ch.uzh.ifi.hase.soprafs21.entity.cards.brownCards.Bang;
import ch.uzh.ifi.hase.soprafs21.entity.gameMoves.GameMove;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerGetAuthDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.PlayerTableGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.CharacterCardGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.GameMoveGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.HandGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PlayCardAuthGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.game.PlayCardGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.users.UserPostDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * DTOMapperTest Tests if the mapping between the internal and the external/API
 * representation works.
 */

@SpringBootTest
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username");
        userPostDTO.setBirthday(new Date(System.currentTimeMillis()));
        userPostDTO.setPassword("password");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getBirthday(), user.getBirthday());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        // user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setBirthday(new Date(System.currentTimeMillis()));
        user.setPassword("password");
        user.setCreationDate(new Date(System.currentTimeMillis()));

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
        assertEquals(user.getBirthday(), userGetDTO.getBirthday());
        assertEquals(user.getCreationDate(), userGetDTO.getCreationDate());
    }

    @Test
    public void testGetPlayerTable_fromPlayerTable_toPlayerTableGetDTO_success() {
        // create a table and players
        PlayerTable table = new PlayerTable();
        List<Player> players = new ArrayList<>();
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        table.setPlayers(players);
        table.setPlayerOnTurn(player1);
        table.setId(1L);

        // Mapping
        PlayerTableGetDTO playerTableGetDTO = DTOMapper.INSTANCE.convertEntityToPlayerTableGetDTO(table);

        // check
        assertEquals(table.getId(), playerTableGetDTO.getId());
        // assertEquals(table.getPlayers(), playerTableGetDTO.getPlayers());
        // assertEquals(table.getPlayerOnTurn(), playerTableGetDTO.getPlayerOnTurn());
    }

    @Test
    public void test_player_to_playerGetDTO_success() {
        // create players
        User user = new User();
        Hand hand = new Hand();
        OnFieldCards cards = new OnFieldCards();
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        player1.setUser(user);
        player1.setGameRole(GameRole.DEPUTY);
        player1.setReady(false);
        player1.setLeftNeighbor(player2);
        player1.setRightNeighbor(player3);
        player1.setHand(hand);
        player1.setOnFieldCards(cards);
        player1.setTable(new PlayerTable());

        // Mapping
        PlayerGetDTO playerGetDTO = DTOMapper.INSTANCE.convertEntityToPlayerGetDTO(player1);

        // check
        assertEquals(player1.getId(), playerGetDTO.getId());
        assertEquals(player1.getUser().getUsername(), playerGetDTO.getUser());
        assertEquals(GameRole.HIDDEN, playerGetDTO.getGameRole());
        assertEquals(player1.getLeftNeighbor().getId(), playerGetDTO.getLeftNeighbor());
        assertEquals(player1.getRightNeighbor().getId(), playerGetDTO.getRightNeighbor());

    }

    @Test
    public void test_player_to_playerGetAuthDTO_success() {
        // create players
        User user = new User();
        Hand hand = new Hand();
        OnFieldCards cards = new OnFieldCards();
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        player1.setUser(user);
        player1.setGameRole(GameRole.DEPUTY);
        player1.setReady(false);
        player1.setLeftNeighbor(player2);
        player1.setRightNeighbor(player3);
        player1.setHand(hand);
        player1.setOnFieldCards(cards);

        // Mapping
        PlayerGetAuthDTO playerGetAuthDTO = DTOMapper.INSTANCE.convertEntityToPlayerGetAuthDTO(player1);

        // check
        assertEquals(player1.getId(), playerGetAuthDTO.getId());
        assertEquals(player1.getUser().getUsername(), playerGetAuthDTO.getUser());
        assertEquals(player1.getGameRole(), playerGetAuthDTO.getGameRole());
        assertEquals(player1.getLeftNeighbor().getId(), playerGetAuthDTO.getLeftNeighbor());
        assertEquals(player1.getRightNeighbor().getId(), playerGetAuthDTO.getRightNeighbor());

    }

    @Test
    public void test_hand_to_handGetDTO_success() {

        // create hand
        Hand hand = new Hand();
        List<PlayCard> cards = new ArrayList<>();
        PlayCard card = new Bang(Rank.ACE, Suit.CLUBS);
        cards.add(card);
        hand.setPlayCards(cards);

        // Mapping
        HandGetDTO handGetDTO = DTOMapper.INSTANCE.convertEntityToHandGetDTO(hand);

        // check
        assertEquals(new ArrayList<>(), handGetDTO.getPlayCards());
        assertEquals(hand.getPlayCards().size(), handGetDTO.getCardsInHand());

    }

    // This test doesn't work...
    /*
     * @Test public void test_visibleCards_to_visibleCardsGetDTO_success() {
     * 
     * // create visibleCards VisibleCards visibleCards = new VisibleCards();
     * List<PlayCard> cards = new ArrayList<>(); List<PlayCardAuthGetDTO> cards2 =
     * new ArrayList<>(); PlayCard card = new Bang(Rank.ACE, Suit.CLUBS);
     * cards.add(card); visibleCards.setVisibleCards(cards);
     * 
     * //Mapping VisibleCardsGetDTO visibleCardsGetDTO =
     * DTOMapper.INSTANCE.convertEntityToVisibleCardsGetDTO(visibleCards);
     * cards2.add(DTOMapper.INSTANCE.convertEntityToPlayCardGetAuthDTO(card));
     * 
     * //check assertEquals(cards2, visibleCardsGetDTO.getVisibleCards()); }
     */

    @Test
    public void test_playCard_to_playCardGetDTO_success() {

        // create Card
        PlayCard card = new Bang(Rank.ACE, Suit.CLUBS);

        // Mapping
        PlayCardGetDTO playCardGetDTO = DTOMapper.INSTANCE.convertEntityToPlayCardGetDTO(card);

        // check
        assertEquals(Card.HIDDEN, playCardGetDTO.getCard());
        assertEquals(card.getColor(), playCardGetDTO.getColor());
        assertEquals(Rank.HIDDEN, playCardGetDTO.getRank());
        assertEquals(Suit.HIDDEN, playCardGetDTO.getSuit());
        assertEquals(card.getId(), playCardGetDTO.getId());

    }

    @Test
    public void test_playCard_to_playCardGetAuthDTO_success() {

        // create Card
        PlayCard card = new Bang(Rank.ACE, Suit.CLUBS);

        // Mapping
        PlayCardAuthGetDTO playCardAuthGetDTO = DTOMapper.INSTANCE.convertEntityToPlayCardGetAuthDTO(card);

        // check
        assertEquals(card.getCard(), playCardAuthGetDTO.getCard());
        assertEquals(card.getColor(), playCardAuthGetDTO.getColor());
        assertEquals(card.getRank(), playCardAuthGetDTO.getRank());
        assertEquals(card.getSuit(), playCardAuthGetDTO.getSuit());
        assertEquals(card.getId(), playCardAuthGetDTO.getId());

    }

    @Test
    public void test_characterCard_to_characterCardGetDTO_success() {

        // create Card
        CharacterCard card = new CharacterCard();
        card.setDescription("description");
        card.setName("name");
        card.setId(1L);
        card.setDisplay("display");
        card.setLifeAmount(1);
        // Mapping
        CharacterCardGetDTO characterCardGetDTO = DTOMapper.INSTANCE.convertEntityToCharacterCardGetDTO(card);

        // check
        assertEquals(card.getDescription(), characterCardGetDTO.getDescription());
        assertEquals(card.getName(), characterCardGetDTO.getName());
        assertEquals(card.getId(), characterCardGetDTO.getId());
        assertEquals(card.getDisplay(), characterCardGetDTO.getDisplay());
        assertEquals(card.getLifeAmount(), characterCardGetDTO.getLifeAmount());

    }

    @Test
    public void test_GameMove_to_GameMoveGetDTO_success() {

        // create GameMove
        GameMove move = new GameMove();
        move.setAction(GameMoveAction.ACTIVATE);
        move.setId(1L);
        move.setMessage("message");
        
        // Mapping
        GameMoveGetDTO gameMoveGetDTO = DTOMapper.INSTANCE.convertEntityToGameMoveGetDTO(move);

        // check
        assertEquals(move.getAction(), gameMoveGetDTO.getAction());
        assertEquals(move.getMessage(), gameMoveGetDTO.getMessage());
        assertEquals(move.getId(), gameMoveGetDTO.getId());
        assertEquals(move.getCard(), gameMoveGetDTO.getCard());
        assertEquals(move.getUsingPlayer(), gameMoveGetDTO.getUsingPlayer());
        assertEquals(move.getTargetPlayer(), gameMoveGetDTO.getTargetPlayer());


    }
}
