package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.publicDistrict.PublicDistrictNotFoundException;

@RestControllerAdvice
public class PublicDistrictExceptionHandler {

  @ExceptionHandler(PublicDistrictNotFoundException.class)
  public ResponseEntity<ApiResponse<String>> handlePublicDistrictNotFoundException(PublicDistrictNotFoundException e) {
    return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
  }

  private ResponseEntity<ApiResponse<String>> buildErrorResponse(String message, HttpStatus status) {
    ApiResponse<String> response = new ApiResponse<>("error", message);
    return new ResponseEntity<>(response, status);
  }
}
