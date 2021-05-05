package ch.uzh.ifi.hase.soprafs21.controller.gameStateControllers;

import org.springframework.beans.factory.annotation.Autowired;

import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.exceptions.IllegalGameStateException;
import ch.uzh.ifi.hase.soprafs21.service.PlayerTableService;

public class EndedController {

    @Autowired
    PlayerTableService playerTableService;

}
