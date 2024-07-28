package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.gatherArticle.*;
import sumcoda.boardbuddy.exception.gatherArticle.NotAuthorOfGatherArticleException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;
import static sumcoda.boardbuddy.builder.ResponseBuilder.buildFailureResponse;

@RestControllerAdvice
public class GatherArticleExceptionHandler {

  // 모집글 저장 예외 처리 핸들러
  @ExceptionHandler(GatherArticleSaveException.class)
  public ResponseEntity<ApiResponse<Void>> handleGatherArticleSaveException(GatherArticleSaveException e) {
    return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // 모집글 조회 예외 처리 핸들러
  @ExceptionHandler(GatherArticleNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleGatherArticleNotFoundException(GatherArticleNotFoundException e) {
    return buildFailureResponse(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  // 모집글 수정 예외 처리 핸들러
  @ExceptionHandler(GatherArticleUpdateException.class)
  public ResponseEntity<ApiResponse<Void>> handleGatherArticleUpdateException(GatherArticleUpdateException e) {
    return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // 작성자 확인 예외 처리 핸들러
  @ExceptionHandler(GatherArticleAccessDeniedException.class)
  public ResponseEntity<ApiResponse<Void>> handleGatherArticleAccessDeniedException(GatherArticleAccessDeniedException e) {
    return buildFailureResponse(e.getMessage(), HttpStatus.FORBIDDEN);
  }

  // 작성자 확인 예외 처리 핸들러
  @ExceptionHandler(GatherArticleClosedException.class)
  public ResponseEntity<ApiResponse<Void>> handleArticleClosedException(GatherArticleClosedException e) {
    return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotAuthorOfGatherArticleException.class)
  public ResponseEntity<ApiResponse<Void>> handleNotAuthorOfGatherArticleException(NotAuthorOfGatherArticleException e) {
    return buildFailureResponse(e.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(GatherArticleRetrievalException.class)
  public ResponseEntity<ApiResponse<Void>> handleGatherArticleRetrievalException(GatherArticleRetrievalException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }


}
