package sumcoda.boardbuddy.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.enumerate.Status;

public class ResponseHandlerUtil {

    private static final Status STATUS_SUCCESS = Status.SUCCESS;

    private static final Status STATUS_FAILURE = Status.FAILURE;

    private static final Status STATUS_ERROR = Status.ERROR;

    private static <T> ResponseEntity<ApiResponse<T>> buildResponse(Status status, T data, String message, HttpStatus httpStatus) {
        ApiResponse<T> response = ApiResponse.createApiResponse(status, data, message);

        return new ResponseEntity<>(response, httpStatus);
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildSuccessResponse(T data, String message, HttpStatus httpStatus) {
        return buildResponse(STATUS_SUCCESS, data, message, httpStatus);
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildFailureResponse(String message, HttpStatus httpStatus) {
        return buildResponse(STATUS_FAILURE, null, message, httpStatus);
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildErrorResponse(String message, HttpStatus httpStatus) {
        return buildResponse(STATUS_ERROR, null, message, httpStatus);

    }
}
