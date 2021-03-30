package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

@Service
@Transactional
public class PlayerTableService {

    @Autowired
    PlayerTableRepository playerTableRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameRoleService gameRoleService;

    /**
     * Creates and adds a <Player> to a preexisting or new <PlayerTable>.
     * 
     * @param id user id
     */
    public PlayerTable addPlayer(Long id) {
        // check if user is already in a game
        if (playerRepository.existsById(id)) {
            throw new IllegalArgumentException("User is already in a game!");
        }

        Player player = new Player();
        User user = userRepository.getOne(id);
        player.setUser(user);
        player.setId(user.getId());
        List<PlayerTable> playerTables = playerTableRepository.findAll();
        // add user to existing playerTable
        for (PlayerTable playerTable : playerTables) {
            List<Player> players = playerTable.getPlayers();
            if (players.size() < 7 && !playerTable.getGameHasStarted()) {
                players.add(player);
                playerTable.setPlayers(players);
                playerTableRepository.save(playerTable);
                playerTableRepository.flush();
                return playerTable;

            }
        }
        // create new playerTable
        PlayerTable playerTable = new PlayerTable();
        List<Player> players = new ArrayList<Player>();
        players.add(player);
        playerTable.setPlayers(players);
        playerTableRepository.save(playerTable);
        playerTableRepository.flush();
        return playerTable;
    }

    public void startGame(Long id) {
        PlayerTable table = playerTableRepository.getOne(id);
        if (table.getPlayers().size() < 4) {
            throw new IllegalArgumentException("Not enough players yet!");
        }
        table.setGameHasStarted(true);
        assignGameRoles(table);

        // give back character cards to choose from
    }

    private void assignGameRoles(PlayerTable table) {
        List<Player> players = table.getPlayers();
        List<GameRole> roles = gameRoleService.getRoles(players.size());
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setGameRole(roles.get(i));
        }
        playerTableRepository.save(table);
    }

    public PlayerTable getPlayerTableById(Long id) {
        return playerTableRepository.getOne(id);
    }

}
