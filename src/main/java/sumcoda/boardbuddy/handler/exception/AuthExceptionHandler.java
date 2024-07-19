package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.exception.auth.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(SMSCertificationExpiredException.class)
    public ResponseEntity<Map<String, Object>> smsCertificationExpiredExceptionHandler(SMSCertificationExpiredException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", false);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SMSCertificationNumberMismatchException.class)
    public ResponseEntity<Map<String, Object>> smsCertificationNumberMismatchExceptionHandler(SMSCertificationNumberMismatchException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", false);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SMSCertificationAttemptExceededException.class)
    public ResponseEntity<Map<String, Object>> smsCertificationAttemptExceededExceptionHandler(SMSCertificationAttemptExceededException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", false);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SMSSendingException.class)
    public ResponseEntity<Map<String, Object>> smsSendingExceptionHandler(SMSSendingException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", false);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> authenticationNotSupportedExceptionHandler(AuthenticationNotSupportedException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", null);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingCredentialsException.class)
    public ResponseEntity<Map<String, Object>> missingCredentialsExceptionHandler(MissingCredentialsException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", null);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SocialUserInfoRetrievalException.class)
    public ResponseEntity<Map<String, Object>> socialUserInfoRetrievalExceptionHandler(SocialUserInfoRetrievalException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", null);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ClientRegistrationRetrievalException.class)
    public ResponseEntity<Map<String, Object>> clientRegistrationRetrievalExceptionHandler(ClientRegistrationRetrievalException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", null);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, Object>> invalidPasswordExceptionHandler(InvalidPasswordException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", false);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationMissingException.class)
    public ResponseEntity<Map<String, Object>> authenticationMissingExceptionHandler(AuthenticationMissingException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", null);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<Map<String, Object>> invalidRequestBodyExceptionHandler(InvalidRequestBodyException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", null);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyLoggedOutException.class)
    public ResponseEntity<Map<String, Object>> alreadyLoggedOutExceptionHandler(AlreadyLoggedOutException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", null);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
