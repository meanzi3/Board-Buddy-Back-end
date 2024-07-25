package sumcoda.boardbuddy.builder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.enumerate.Status;

import java.util.Map;

public class ResponseBuilder {

    private static final Status STATUS_SUCCESS = Status.SUCCESS;

    private static final Status STATUS_FAILURE = Status.FAILURE;

    private static final Status STATUS_ERROR = Status.ERROR;

    private static <T> ResponseEntity<ApiResponse<Map<String, T>>> buildResponseWithPairKeyData(Status status, String key, T data, String message, HttpStatus httpStatus) {
        ApiResponse<Map<String, T>> response = ApiResponse.<Map<String, T>>builder()
                .status(status.getValue())
                .data(Map.of(key, data))
                .message(message)
                .build();
        return new ResponseEntity<>(response, httpStatus);
    }

    private static <T> ResponseEntity<ApiResponse<T>> buildResponseWithMultiplePairKeyData(Status status, T data, String message, HttpStatus httpStatus) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(status.getValue())
                .data(data)
                .message(message)
                .build();
        return new ResponseEntity<>(response, httpStatus);
    }

    private static <T> ResponseEntity<ApiResponse<T>> buildResponseWithoutData(Status status, String message, HttpStatus httpStatus) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .status(status.getValue())
                .data(null)
                .message(message)
                .build();
        return new ResponseEntity<>(response, httpStatus);
    }

    public static <T> ResponseEntity<ApiResponse<Map<String, T>>> buildSuccessResponseWithPairKeyData(String key, T data, String message, HttpStatus httpStatus) {
        return buildResponseWithPairKeyData(STATUS_SUCCESS, key, data, message, httpStatus);
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildSuccessResponseWithMultiplePairKeyData(T data, String message, HttpStatus httpStatus) {
        return buildResponseWithMultiplePairKeyData(STATUS_SUCCESS, data, message, httpStatus);
    }

    public static ResponseEntity<ApiResponse<Void>> buildSuccessResponseWithoutData(String message, HttpStatus httpStatus) {
        return buildResponseWithoutData(STATUS_SUCCESS, message, httpStatus);
    }

    public static ResponseEntity<ApiResponse<Void>> buildFailureResponse(String message, HttpStatus httpStatus) {
        return buildResponseWithoutData(STATUS_FAILURE, message, httpStatus);
    }

    public static ResponseEntity<ApiResponse<Void>> buildErrorResponse(String message, HttpStatus httpStatus) {
        return buildResponseWithoutData(STATUS_ERROR, message, httpStatus);
    }
}
