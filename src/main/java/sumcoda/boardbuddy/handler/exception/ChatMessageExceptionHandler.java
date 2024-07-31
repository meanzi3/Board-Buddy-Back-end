package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.ChatMessageRetrievalException;
import sumcoda.boardbuddy.exception.ChatMessageSaveException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;

@RestControllerAdvice
public class ChatMessageExceptionHandler {

    @ExceptionHandler(ChatMessageRetrievalException.class)
    public ResponseEntity<ApiResponse<Void>> handleChatMessageRetrievalException(ChatMessageRetrievalException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ChatMessageSaveException.class)
    public ResponseEntity<ApiResponse<Void>> handleChatMessageSaveException(ChatMessageSaveException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
