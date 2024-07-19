package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.member.*;

import static sumcoda.boardbuddy.util.ResponseHandlerUtil.*;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleNicknameAlreadyExistsException(NicknameAlreadyExistsException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MemberSaveException.class)
    public ResponseEntity<ApiResponse<Object>> handleMemberSaveException(MemberSaveException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MemberRetrievalException.class)
    public ResponseEntity<ApiResponse<Object>> handleMemberNotFoundException(MemberRetrievalException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MemberDeletionFailureException.class)
    public ResponseEntity<ApiResponse<Object>> handleMemberDeletionFailureException(MemberDeletionFailureException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
