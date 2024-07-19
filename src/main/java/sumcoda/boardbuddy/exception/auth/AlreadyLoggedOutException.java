package sumcoda.boardbuddy.exception.auth;

public class AlreadyLoggedOutException extends RuntimeException {
    public AlreadyLoggedOutException(String message) {
        super(message);
    }
}
