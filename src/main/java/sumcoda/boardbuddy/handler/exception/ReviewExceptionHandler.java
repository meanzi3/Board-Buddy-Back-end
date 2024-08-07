package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.review.ReviewAlreadyExistsException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildFailureResponse;

@RestControllerAdvice
public class ReviewExceptionHandler {

    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleReviewAlreadyExistsException(ReviewAlreadyExistsException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
