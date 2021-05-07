package ch.uzh.ifi.hase.soprafs21.controller.gameStateControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.entity.Player;
import ch.uzh.ifi.hase.soprafs21.entity.PlayerTable;
import ch.uzh.ifi.hase.soprafs21.repository.PlayerTableRepository;
import ch.uzh.ifi.hase.soprafs21.service.PlayerTableService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

@RestController
@RequestMapping("/api/v1/games")
public class EndedController {

    @Autowired
    UserService userService;

    @Autowired
    PlayerTableService playerTableService;

}
