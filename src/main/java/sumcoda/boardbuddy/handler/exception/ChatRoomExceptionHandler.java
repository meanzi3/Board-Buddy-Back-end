package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.*;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;
import static sumcoda.boardbuddy.builder.ResponseBuilder.buildFailureResponse;

@RestControllerAdvice
public class ChatRoomExceptionHandler {

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleChatRoomNotFoundException(ChatRoomNotFoundException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ChatRoomHostCannotLeaveException.class)
    public ResponseEntity<ApiResponse<Void>> handleChatRoomHostCannotLeaveException(ChatRoomHostCannotLeaveException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ChatRoomRetrievalException.class)
    public ResponseEntity<ApiResponse<Void>> handleChatRoomRetrievalException(ChatRoomRetrievalException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AlreadyEnteredChatRoomException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyEnteredChatRoomException(AlreadyEnteredChatRoomException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ChatRoomAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleChatRoomAccessDeniedException(ChatRoomAccessDeniedException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
