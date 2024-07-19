package sumcoda.boardbuddy.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.enumerate.Status;

public class ResponseHandlerUtil {

    private static final Status STATUS_SUCCESS = Status.SUCCESS;

    private static final Status STATUS_FAILURE = Status.FAILURE;

    private static final Status STATUS_ERROR = Status.ERROR;


    public static ResponseEntity<ApiResponse<Object>> buildSuccessResponse(Object data, String message, HttpStatus httpStatus) {
        ApiResponse<Object> response = ApiResponse.builder()
                .status(STATUS_SUCCESS.getValue())
                .data(data)
                .message(message)
                .build();

        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<ApiResponse<Object>> buildFailureOrErrorResponse(Status status, String message, HttpStatus httpStatus) {
        ApiResponse<Object> response = ApiResponse.builder()
                .status(status.getValue())
                .data(null)
                .message(message)
                .build();

        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<ApiResponse<Object>> buildFailureResponse(String message, HttpStatus httpStatus) {
        return buildFailureOrErrorResponse(STATUS_FAILURE, message, httpStatus);
    }

    public static ResponseEntity<ApiResponse<Object>> buildErrorResponse(String message, HttpStatus httpStatus) {
        return buildFailureOrErrorResponse(STATUS_ERROR, message, httpStatus);

    }
}
