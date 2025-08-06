package exception;

/** Lanciata quando un DAO riceve chiavi non valide. */
public class InvalidDaoKeyException extends RuntimeException {
    public InvalidDaoKeyException(String message) { super(message); }
}