package sumcoda.boardbuddy.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.client.MemberAuthProfileDTO;
import sumcoda.boardbuddy.dto.fetch.MemberAuthProfileProjection;
import sumcoda.boardbuddy.exception.auth.AuthenticationMissingException;
import sumcoda.boardbuddy.mapper.MemberProfileMapper;
import sumcoda.boardbuddy.repository.member.MemberRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    private final MemberProfileMapper memberProfileMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("success handler is working");

        final ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> responseData = new HashMap<>();

        if (authentication == null) {
            throw new AuthenticationMissingException("인증 객체가 누락되었습니다. 관리자에게 문의하세요.");
        }

        UserDetails user = (UserDetails) authentication.getPrincipal();

        String username = user.getUsername();

        log.info("login user name : " + username);

        MemberAuthProfileProjection projection = memberRepository.findMemberAuthProfileByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("해당 아이디를 가진 사용자가 존재하지 않습니다. : " + username));

        MemberAuthProfileDTO memberAuthProfileDTO = memberProfileMapper.toMemberAuthProfileDTO(projection);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        responseData.put("status", "success");
        responseData.put("data", Map.of("memberAuthProfileDTO", memberAuthProfileDTO));
        responseData.put("message", "로그인에 성공하였습니다.");

        objectMapper.writeValue(response.getWriter(), responseData);
    }
}

