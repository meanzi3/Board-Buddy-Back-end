package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.boardCafe.BoardCafeRadiusException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildFailureResponse;

@RestControllerAdvice
public class BoardCafeExceptionHandler {

    // 보드 게임 카페 찾기 반경 설정 예외 처리 핸들러
    @ExceptionHandler(BoardCafeRadiusException.class)
    public ResponseEntity<ApiResponse<Void>> handleBoardCafeRadiusException(BoardCafeRadiusException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
