package sumcoda.boardbuddy.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.exception.auth.SMSCertificationAttemptExceededException;
import sumcoda.boardbuddy.exception.auth.SMSCertificationExpiredException;
import sumcoda.boardbuddy.exception.auth.SMSCertificationNumberMismatchException;
import sumcoda.boardbuddy.exception.auth.SMSSendingException;

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
}
