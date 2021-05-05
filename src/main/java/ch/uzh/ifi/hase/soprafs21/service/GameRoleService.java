package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs21.constant.GameRole;
import ch.uzh.ifi.hase.soprafs21.entity.Player;

@Service
public class GameRoleService {

    public List<GameRole> getRoles(Integer numberOfPlayers) {
        List<GameRole> roles = new ArrayList<GameRole>();
        roles.add(GameRole.SHERIFF);
        roles.add(GameRole.OUTLAW);
        roles.add(GameRole.OUTLAW);
        roles.add(GameRole.RENEGADE);
        roles.add(GameRole.DEPUTY);
        roles.add(GameRole.OUTLAW);
        roles.add(GameRole.DEPUTY);

        List<GameRole> gameRoles = roles.subList(0, numberOfPlayers);
        Collections.shuffle(gameRoles);
        return gameRoles;
    }
}
