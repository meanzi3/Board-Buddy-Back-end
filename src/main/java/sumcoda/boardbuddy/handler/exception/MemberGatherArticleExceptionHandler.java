package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.memberGatherArticle.*;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;
import static sumcoda.boardbuddy.builder.ResponseBuilder.buildFailureResponse;

@RestControllerAdvice
public class MemberGatherArticleExceptionHandler {

    @ExceptionHandler(MemberGatherArticleSaveException.class)
    public ResponseEntity<ApiResponse<Void>> handleMemberGatherArticleSaveException(MemberGatherArticleSaveException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MemberGatherArticleAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleMemberGatherArticleAlreadyExistsException(MemberGatherArticleAlreadyExistsException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberGatherArticleNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleMemberGatherArticleNotFoundException(MemberGatherArticleNotFoundException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemberGatherArticleRetrievalException.class)
    public ResponseEntity<ApiResponse<Void>> handleMemberGatherArticleRetrievalException(MemberGatherArticleRetrievalException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
