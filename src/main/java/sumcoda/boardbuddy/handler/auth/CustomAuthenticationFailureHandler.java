package sumcoda.boardbuddy.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.exception.auth.InvalidPasswordException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("authentication failure handler is working");
        Map<String, Object> responseData = new HashMap<>();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String errorMessage = getErrorMessage(response, exception);

        responseData.put("status", "failure");
        responseData.put("data", null);
        responseData.put("message", errorMessage);

        objectMapper.writeValue(response.getWriter(), responseData);
    }

    private static String getErrorMessage(HttpServletResponse response, AuthenticationException exception) {
        String errorMessage;

        if (exception instanceof BadCredentialsException ||
                exception instanceof UsernameNotFoundException ||
                exception instanceof InvalidPasswordException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            if (exception instanceof BadCredentialsException) {
                errorMessage = "입력한 회원 정보가 올바르지 않습니다. 올바른 회원정보를 입력하세요.";
            } else {
                errorMessage = exception.getMessage();
            }
        } else if (exception instanceof InternalAuthenticationServiceException) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

            errorMessage = "내부 시스템 문제로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());

            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            errorMessage = "인증에 실패했습니다.";
        }
        //        else if(exception instanceof DisabledException) {
//            errorMessage = "Locked";
//        }
//        else if(exception instanceof CredentialsExpiredException) {
//            errorMessage = "Expired password";
//        }

        return errorMessage;
    }
}

