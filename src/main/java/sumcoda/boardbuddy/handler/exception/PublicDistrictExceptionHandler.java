package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.publicDistrict.NoSearchResultException;
import sumcoda.boardbuddy.exception.publicDistrict.PublicDistrictRetrievalException;
import sumcoda.boardbuddy.exception.publicDistrict.SearchLengthException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;

@RestControllerAdvice
public class PublicDistrictExceptionHandler {

  // 행정 구역 찾기 예외 처리 핸들러
  @ExceptionHandler(PublicDistrictRetrievalException.class)
  public ResponseEntity<ApiResponse<Void>> handlePublicDistrictRetrievalException(PublicDistrictRetrievalException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // 검색어 길이 예외 처리 핸들러
  @ExceptionHandler(SearchLengthException.class)
  public ResponseEntity<ApiResponse<Void>> handleSearchLengthException(SearchLengthException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // 검색 결과 없음 예외 처리 핸들러
  @ExceptionHandler(NoSearchResultException.class)
  public ResponseEntity<ApiResponse<Void>> handleNoSearchResultException(NoSearchResultException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
  }
}
