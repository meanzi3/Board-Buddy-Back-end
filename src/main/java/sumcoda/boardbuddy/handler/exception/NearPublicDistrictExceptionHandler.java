package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.nearPublicDistrict.NearPublicDistrictRetrievalException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;

public class NearPublicDistrictExceptionHandler {

    // 주변 행정 구역 찾기 예외 처리 핸들러
    @ExceptionHandler(NearPublicDistrictRetrievalException.class)
    public ResponseEntity<ApiResponse<Void>> handleNearPublicDistrictRetrievalException(NearPublicDistrictRetrievalException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
