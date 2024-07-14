package sumcoda.boardbuddy.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sumcoda.boardbuddy.exception.member.MemberSaveException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberSaveException.class)
    public ResponseEntity<Map<String, Object>> memberSaveExceptionHandler(MemberSaveException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("data", false);

        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
