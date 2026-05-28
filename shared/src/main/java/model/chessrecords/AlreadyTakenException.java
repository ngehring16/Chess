package model.chessrecords;

public class AlreadyTakenException extends Exception {
    public AlreadyTakenException() {
        super("This option is already taken! Please try another one.");
    }
}
