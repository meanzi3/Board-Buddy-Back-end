package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleNotFoundException;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleSaveException;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleUpdateException;
import sumcoda.boardbuddy.exception.gatherArticle.UnauthorizedActionException;

@RestControllerAdvice
public class GatherArticleExceptionHandler {

  // 모집글 저장 예외 처리 핸들러
  @ExceptionHandler(GatherArticleSaveException.class)
  public ResponseEntity<ApiResponse<String>> handleGatherArticleSaveException(GatherArticleSaveException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // 모집글 조회 예외 처리 핸들러
  @ExceptionHandler(GatherArticleNotFoundException.class)
  public ResponseEntity<ApiResponse<String>> handleGatherArticleNotFoundException(GatherArticleNotFoundException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  // 모집글 수정 예외 처리 핸들러
  @ExceptionHandler(GatherArticleUpdateException.class)
  public ResponseEntity<ApiResponse<String>> handleGatherArticleUpdateException(GatherArticleUpdateException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // 작성자 확인 예외 처리 핸들러
  @ExceptionHandler(UnauthorizedActionException.class)
  public ResponseEntity<ApiResponse<String>> handleUnauthorizedActionException(UnauthorizedActionException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN);
  }

  private ResponseEntity<ApiResponse<String>> buildErrorResponse(String message, HttpStatus status) {
    ApiResponse<String> response = new ApiResponse<>("error", message);
    return new ResponseEntity<>(response, status);
  }
}
