package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.participationApplication.ParticipationApplicationNotPendingOrApproveException;
import sumcoda.boardbuddy.exception.participationApplication.CannotReApplyAfterCancellationException;
import sumcoda.boardbuddy.exception.participationApplication.*;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;
import static sumcoda.boardbuddy.builder.ResponseBuilder.buildFailureResponse;

@RestControllerAdvice
public class ParticipationApplicationExceptionHandler {

    @ExceptionHandler(ParticipationApplicationRetrievalException.class)
    public ResponseEntity<ApiResponse<Void>> handleParticipationApplicationRetrievalException(ParticipationApplicationRetrievalException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ParticipationApplicationNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleParticipationApplicationNotFoundException(ParticipationApplicationNotFoundException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ParticipationApplicationSaveException.class)
    public ResponseEntity<ApiResponse<Void>> handleParticipationApplicationSaveException(ParticipationApplicationSaveException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ParticipationApplicationLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleParticipationApplicationLimitExceededException(ParticipationApplicationLimitExceededException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MaxRejectionsExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxRejectionsExceededException(MaxRejectionsExceededException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AlreadyApprovedParticipantException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyApprovedParticipantException(AlreadyApprovedParticipantException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AlreadyCancelledParticipantException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyCancelledParticipantException(AlreadyCancelledParticipantException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorCannotCancelApplicationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthorCannotCancelApplicationException(AuthorCannotCancelApplicationException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CannotReApplyAfterCancellationException.class)
    public ResponseEntity<ApiResponse<Void>> handleCannotReApplyAfterCancellationException(CannotReApplyAfterCancellationException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ParticipationApplicationNotPendingOrApproveException.class)
    public ResponseEntity<ApiResponse<Void>> handleParticipationApplicationNotPendingOrApprovedException(ParticipationApplicationNotPendingOrApproveException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
