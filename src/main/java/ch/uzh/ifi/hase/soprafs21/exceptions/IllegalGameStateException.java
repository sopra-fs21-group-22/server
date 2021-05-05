package ch.uzh.ifi.hase.soprafs21.exceptions;

import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;

public class IllegalGameStateException extends RuntimeException {
    public IllegalGameStateException(GameStatus currState) {
        super(String.format("Action not possible in state %s!", currState));
    }
}
