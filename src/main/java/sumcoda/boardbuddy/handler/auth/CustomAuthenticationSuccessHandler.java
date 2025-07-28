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
import sumcoda.boardbuddy.dto.MemberAuthProfileDTO;
import sumcoda.boardbuddy.dto.fetch.MemberAuthProfileProjection;
import sumcoda.boardbuddy.exception.auth.AuthenticationMissingException;
import sumcoda.boardbuddy.repository.member.MemberRepository;
import sumcoda.boardbuddy.service.CloudFrontSignedUrlService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static sumcoda.boardbuddy.util.MemberProfileUtil.convertMemberAuthProfileDTO;
import static sumcoda.boardbuddy.util.ProfileImageUtil.buildProfileImageS3RequestKey;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    private final CloudFrontSignedUrlService cloudFrontSignedUrlService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("success handler is working");

        if (authentication == null) {
            throw new AuthenticationMissingException("인증 객체가 누락되었습니다. 관리자에게 문의하세요.");
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseData = new HashMap<>();

        UserDetails user =  (UserDetails) authentication.getPrincipal();

        String username = user.getUsername();

        log.info("login user name : " + username);

        MemberAuthProfileProjection projection = memberRepository.findMemberAuthProfileByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("해당 아이디를 가진 사용자가 존재하지 않습니다. : " + username));

        String profileImageS3SavedPath = buildProfileImageS3RequestKey(projection.s3SavedObjectName());

        String profileImageSignedURL = cloudFrontSignedUrlService.generateSignedUrl(profileImageS3SavedPath);

        MemberAuthProfileDTO memberAuthProfileDTO = convertMemberAuthProfileDTO(projection, profileImageSignedURL);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        responseData.put("status", "success");
        responseData.put("data", Map.of("memberAuthProfileDTO", memberAuthProfileDTO));
        responseData.put("message", "로그인에 성공하였습니다.");

        objectMapper.writeValue(response.getWriter(), responseData);
    }
}

