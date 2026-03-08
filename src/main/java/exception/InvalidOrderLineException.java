package exception;

public class InvalidOrderLineException extends Exception {
    public InvalidOrderLineException(String message) {
        super(message);
    }
}