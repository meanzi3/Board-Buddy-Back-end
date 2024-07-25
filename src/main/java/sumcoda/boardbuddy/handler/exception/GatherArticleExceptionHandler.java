package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.gatherArticle.*;

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

  // 유효하지 않은 정렬 기준 예외 처리 핸들러
  @ExceptionHandler(GatherArticleSortException.class)
  public ResponseEntity<ApiResponse<Void>> handleGatherArticleSortException(GatherArticleSortException e) {
    return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // 유효하지 않은 모집글 상태 예외 처리 핸들러
  @ExceptionHandler(GatherArticleStatusException.class)
  public ResponseEntity<ApiResponse<Void>> handleGatherArticleStatusException(GatherArticleStatusException e) {
    return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
