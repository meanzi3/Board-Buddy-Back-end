package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.profileImage.ProfileImageDeleteException;
import sumcoda.boardbuddy.exception.profileImage.ProfileImageRetrievalException;
import sumcoda.boardbuddy.exception.profileImage.ProfileImageSaveException;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildErrorResponse;

public class ProfileImageExceptionHandler {

    @ExceptionHandler(ProfileImageSaveException.class)
    public ResponseEntity<ApiResponse<Void>> handleProfileImageSaveException(ProfileImageSaveException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ProfileImageRetrievalException.class)
    public ResponseEntity<ApiResponse<Void>> handleProfileImageRetrievalException(ProfileImageRetrievalException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ProfileImageDeleteException.class)
    public ResponseEntity<ApiResponse<Void>> handleProfileImageDeleteException(ProfileImageDeleteException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
