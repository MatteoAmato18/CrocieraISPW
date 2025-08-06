package exception;

public class UserAlreadyInsertedException extends RuntimeException {
    public UserAlreadyInsertedException(String message, Throwable cause) {
        super(message, cause);
    }
    public UserAlreadyInsertedException(String message) {
        super(message);
    }
}