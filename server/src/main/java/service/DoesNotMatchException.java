package service;

public class DoesNotMatchException extends Exception {
    public DoesNotMatchException(String message) {
        super(message);
    }
}
