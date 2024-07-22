package sumcoda.boardbuddy.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.info("access denied handler is working");

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> responseData = new HashMap<>();

        response.setStatus(HttpStatus.FORBIDDEN.value());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setCharacterEncoding("UTF-8");

        responseData.put("status", "failure");

        responseData.put("data", null);

        responseData.put("message", "해당 요청에 대한 접근이 거부되었습니다.");

        objectMapper.writeValue(response.getWriter(), responseData);
    }
}
