package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.GatherArticleRequest;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleNotFoundException;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleSaveException;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleUpdateException;
import sumcoda.boardbuddy.exception.gatherArticle.UnauthorizedActionException;
import sumcoda.boardbuddy.exception.member.InvalidUserException;

@RestControllerAdvice
public class GatherArticleExceptionHandler {

  // 유효하지 않은 사용자 예외 처리 핸들러
  @ExceptionHandler(InvalidUserException.class)
  public ResponseEntity<ApiResponse<String>> handleInvalidUserException(InvalidUserException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // 모집글 저장 예외 처리 핸들러
  @ExceptionHandler(GatherArticleSaveException.class)
  public ResponseEntity<ApiResponse<String>> handleGatherArticleSaveException(GatherArticleSaveException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // 모집글 조회 예외 처리 핸들러
  @ExceptionHandler(GatherArticleNotFoundException.class)
  public ResponseEntity<ApiResponse<String>> handleGatherArticleNotFoundException(GatherArticleNotFoundException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // 모집글 수정 예외 처리 핸들러
  @ExceptionHandler(GatherArticleUpdateException.class)
  public ResponseEntity<ApiResponse<String>> handleGatherArticleUpdateException(GatherArticleUpdateException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // 작성자 확인 예외 처리 핸들러
  @ExceptionHandler(UnauthorizedActionException.class)
  public ResponseEntity<ApiResponse<String>> handleUnauthorizedActionException(UnauthorizedActionException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ApiResponse<String>> buildErrorResponse(String message, HttpStatus status) {
    ApiResponse<String> response = new ApiResponse<>("error", message);
    return new ResponseEntity<>(response, status);
  }
}
