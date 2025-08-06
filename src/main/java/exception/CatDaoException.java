package exception;

public class CatDaoException extends RuntimeException {
    public CatDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
