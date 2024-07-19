package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.exception.auth.*;

import static sumcoda.boardbuddy.util.ResponseHandlerUtil.*;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(SMSCertificationExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleSMSCertificationExpiredException(SMSCertificationExpiredException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SMSCertificationNumberMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleSMSCertificationNumberMismatchException(SMSCertificationNumberMismatchException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SMSCertificationAttemptExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleSMSCertificationAttemptExceededException(SMSCertificationAttemptExceededException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SMSSendingException.class)
    public ResponseEntity<ApiResponse<Object>> handleSMSSendingExceptionHandler(SMSSendingException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationNotSupportedException(AuthenticationNotSupportedException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MissingCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingCredentialsException(MissingCredentialsException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SocialUserInfoRetrievalException.class)
    public ResponseEntity<ApiResponse<Object>> handleSocialUserInfoRetrievalException(SocialUserInfoRetrievalException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ClientRegistrationRetrievalException.class)
    public ResponseEntity<ApiResponse<Object>> handleClientRegistrationRetrievalException(ClientRegistrationRetrievalException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidPasswordException(InvalidPasswordException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationMissingException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationMissingException(AuthenticationMissingException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidRequestBodyException(InvalidRequestBodyException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyLoggedOutException.class)
    public ResponseEntity<ApiResponse<Object>> handleAlreadyLoggedOutException(AlreadyLoggedOutException e) {
        return buildFailureResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
