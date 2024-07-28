package sumcoda.boardbuddy.exception.participationApplication;

public class CannotReApplyAfterCancellationException extends RuntimeException {
    public CannotReApplyAfterCancellationException(String message) {
        super(message);
    }
}
