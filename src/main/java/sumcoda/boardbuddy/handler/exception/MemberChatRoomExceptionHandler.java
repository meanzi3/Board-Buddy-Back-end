package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.MemberChatRoomNotFoundException;
import sumcoda.boardbuddy.exception.MemberChatRoomRetrievalException;
import sumcoda.boardbuddy.exception.MemberChatRoomSaveException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;
import static sumcoda.boardbuddy.builder.ResponseBuilder.buildFailureResponse;

@RestControllerAdvice
public class MemberChatRoomExceptionHandler {

    @ExceptionHandler(MemberChatRoomNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleMemberChatRoomNotFoundException(MemberChatRoomNotFoundException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberChatRoomRetrievalException.class)
    public ResponseEntity<ApiResponse<Void>> handleMemberChatRoomRetrievalException(MemberChatRoomRetrievalException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MemberChatRoomSaveException.class)
    public ResponseEntity<ApiResponse<Void>> handleMemberChatRoomSaveException(MemberChatRoomSaveException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
