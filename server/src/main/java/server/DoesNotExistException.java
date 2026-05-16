package server;

public class DoesNotExistException extends Exception {
    public DoesNotExistException(String message) {
        super(message);
    }
}
