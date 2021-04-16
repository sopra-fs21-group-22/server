package ch.uzh.ifi.hase.soprafs21.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.exceptions.GameLogicException;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerRepository;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    public void updatePlayerDistance() {
        // TODO
    }

    public void attackPlayer(Player attacker, Player target) {
        if (attacker.getStillPlayableBangsThisRound() <= 0) {
            throw new GameLogicException("Can't play more BANG cards this round!");
        }
        if (!attacker.reachesWithWeapon(target)) {
            throw new GameLogicException("Player is out of range!");
        }
        if (attacker.getId().equals(target.getId())) {
            throw new GameLogicException("Player can't attack himself!");
        }

        // TODO check if target can defend
        target.setBullets(target.getBullets() - 1);
        attacker.setStillPlayableBangsThisRound(attacker.getStillPlayableBangsThisRound() - 1);
        playerRepository.save(target);
        playerRepository.save(attacker);
    }
}
