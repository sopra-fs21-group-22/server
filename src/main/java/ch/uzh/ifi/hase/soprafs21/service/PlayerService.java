package ch.uzh.ifi.hase.soprafs21.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;

@Service
public class PlayerService {

    @Autowired
    DeckService DeckService;

    @Autowired
    PlayerRepository playerRepository;

    public void updatePlayerDistance() {
        // TODO
    }

    public void attackPlayerInRange(Player attacker, Player target) {
        if (!attacker.reachesWithWeapon(target)) {
            throw new GameLogicException("Player is out of range!");
        }
        attackPlayer(attacker, target);
    }

    public void attackPlayer(Player attacker, Player target) {
        if (attacker.getId().equals(target.getId())) {
            throw new GameLogicException("Player can't attack himself!");
        }

        // TODO check if target can defend

        target.setBullets(target.getBullets() - 1);
        playerRepository.save(target);
        playerRepository.save(attacker);
    }
}
