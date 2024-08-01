package sumcoda.boardbuddy.exception.sseEmitter;

public class SseEmitterSendErrorException extends RuntimeException {
    public SseEmitterSendErrorException(String message) {
        super(message);
    }
}
