package ch.uzh.ifi.hase.soprafs21.exceptions;

public class NotOnTurnException extends RuntimeException {
    public NotOnTurnException() {
        super("Player is not on turn!");
    }
}
