package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.RegionNotFoundException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;

@RestControllerAdvice
public class RegionExceptionHandler {

    // 존재하지 않는 시/도 코드(provinceCode)가 요청되었을때 동작하는 예외 처리 핸들러
    @ExceptionHandler(RegionNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRegionNotFoundException(RegionNotFoundException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
