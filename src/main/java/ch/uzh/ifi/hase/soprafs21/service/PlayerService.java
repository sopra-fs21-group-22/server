package ch.uzh.ifi.hase.soprafs21.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antlr.collections.List;
import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    public void updatePlayerDistance() {
        // TODO
    }

    public void attackPlayer(Player attacker, Player target) {
        if (attacker.reachesWithWeapon(target) && attacker.getStillPlayableBangsThisRound() > 0) {
            throw new IllegalArgumentException("Player is out of range!");
        }
        // TODO check if target can defend
        target.setBullets(target.getBullets() - 1);
        attacker.setStillPlayableBangsThisRound(attacker.getStillPlayableBangsThisRound() - 1);
        playerRepository.save(target);
        playerRepository.save(attacker);
    }
}
