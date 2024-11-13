package sumcoda.boardbuddy.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import sumcoda.boardbuddy.dto.AuthRequest;
import sumcoda.boardbuddy.dto.auth.CustomAuthenticationToken;
import sumcoda.boardbuddy.exception.auth.AuthenticationNotSupportedException;
import sumcoda.boardbuddy.exception.auth.InvalidRequestBodyException;
import sumcoda.boardbuddy.exception.auth.MissingCredentialsException;

import java.io.IOException;


@Log4j2
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public CustomAuthenticationFilter() {
        // url과 일치할 경우 해당 필터가 동작합니다.
        super(new AntPathRequestMatcher("/v1/auth/login"));
    }

    // 요청으로 받은 유저 정보를 바탕으로 로그인 인증 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper objectMapper = new ObjectMapper();

        // 해당 요청이 POST 인지 확인
        if(!isPost(request)) {
            throw new AuthenticationNotSupportedException("해당 요청은 인증이 지원되지 않는 요청입니다. POST 요청인지 확인하세요.");
        }

        // POST 이면 body 를 AccountDto( 로그인 정보 DTO ) 에 매핑
        AuthRequest.LoginDTO loginDTO;
        try {
            loginDTO = objectMapper.readValue(request.getReader(), AuthRequest.LoginDTO.class);
        } catch (IOException e) {
            throw new InvalidRequestBodyException("요청 본문이 잘못된 형식이거나 유효하지 않은 데이터를 포함하고 있습니다. 올바른 형식으로 요청을 다시 시도하세요.");
        }

        // ID, PASSWORD 가 있는지 확인
        if(!StringUtils.hasLength(loginDTO.getUsername())
                || !StringUtils.hasLength(loginDTO.getPassword())) {
            throw new MissingCredentialsException("아이디나 비밀번호의 입력이 누락되었습니다. 아이디와 비밀번호를 올바르게 입력했는지 확인해주세요.");
        }

        // 처음에는 인증 되지 않은 토큰 생성
        CustomAuthenticationToken token = new CustomAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );

        // Manager 에게 인증 처리
        return getAuthenticationManager().authenticate(token);
    }

    // 해당 요청이 POST 요청인지 검증
    private boolean isPost(HttpServletRequest request) {
        return "POST".equals(request.getMethod());
    }
}