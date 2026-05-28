package model.chessrecords;

public class DoesNotMatchException extends Exception {
    public DoesNotMatchException() {
        super("This username and password do not match! Pleae try again.");
    }
}
