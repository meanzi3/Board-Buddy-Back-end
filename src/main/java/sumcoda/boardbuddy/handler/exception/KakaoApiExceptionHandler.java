package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.kakaoApi.KakaoApiServerException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;

@RestControllerAdvice
public class KakaoApiExceptionHandler {

    // 카카오 API 서버 문제 예외 처리 핸들러
    @ExceptionHandler(KakaoApiServerException.class)
    public ResponseEntity<ApiResponse<Void>> handleKakaoApiServerException(KakaoApiServerException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_GATEWAY);
    }
}
