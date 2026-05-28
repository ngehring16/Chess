package server;

public class AlreadyTakenException extends Exception {
    public AlreadyTakenException() {
        super("This username is already taken! Please try another one.");
    }
}
