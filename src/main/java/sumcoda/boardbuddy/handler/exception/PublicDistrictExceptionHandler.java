package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.publicDistrict.PublicDistrictRetrievalException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;

@RestControllerAdvice
public class PublicDistrictExceptionHandler {

  // 행정 구역 찾기 예외 처리 핸들러
  @ExceptionHandler(PublicDistrictRetrievalException.class)
  public ResponseEntity<ApiResponse<Void>> handlePublicDistrictRetrievalException(PublicDistrictRetrievalException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
