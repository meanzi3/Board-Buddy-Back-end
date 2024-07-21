package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.publicDistrict.PublicDistrictNotFoundException;

import static sumcoda.boardbuddy.util.ResponseHandlerUtil.buildErrorResponse;

@RestControllerAdvice
public class PublicDistrictExceptionHandler {

  // 행정 구역 찾기 예외 처리 핸들러
  @ExceptionHandler(PublicDistrictNotFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handlePublicDistrictNotFoundException(PublicDistrictNotFoundException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
  }
}
