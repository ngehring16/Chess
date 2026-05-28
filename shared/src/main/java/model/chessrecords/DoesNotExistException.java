package model.chessrecords;

public class DoesNotExistException extends Exception {
    public DoesNotExistException() {
        super("This username does not exist! Please try again.");
    }
}
