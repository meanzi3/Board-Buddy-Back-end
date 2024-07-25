package sumcoda.boardbuddy.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import sumcoda.boardbuddy.dto.auth.oauth2.CustomOAuth2User;
import sumcoda.boardbuddy.exception.auth.AuthenticationMissingException;

@Slf4j
public class AuthUtil {

    /**
     * 로그인 유형에 따라 사용자의 이름을 반환
     *
     * @param authentication 로그인 정보를 포함하는 사용자 객체
     * @return 사용자의 이름.
     */
    public static String getUserNameByLoginType(Authentication authentication) {

//        if (!authentication.isAuthenticated()) {
//            throw new AuthenticationMissingException("유효하지 않은 사용자의 요청입니다.(인터셉터 동작)");
//        }

        String username;

        if (authentication instanceof OAuth2AuthenticationToken) {
            // OAuth2.0 사용자
            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
            username = oauthUser.getUsername();

            // 그외 사용자
        } else {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }

        return username;
    }
}
