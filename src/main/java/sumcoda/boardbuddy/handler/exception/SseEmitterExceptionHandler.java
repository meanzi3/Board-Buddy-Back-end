package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.sseEmitter.SseEmitterSendErrorException;
import sumcoda.boardbuddy.exception.sseEmitter.SseEmitterSubscribeErrorException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;

@RestControllerAdvice
public class SseEmitterExceptionHandler {

    @ExceptionHandler(SseEmitterSubscribeErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleSseEmitterSubscribeErrorException(SseEmitterSubscribeErrorException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SseEmitterSendErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleSseEmitterSendErrorException(SseEmitterSendErrorException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
