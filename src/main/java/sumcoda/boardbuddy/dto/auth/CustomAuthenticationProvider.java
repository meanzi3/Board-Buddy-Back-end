package sumcoda.boardbuddy.dto.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.exception.auth.AuthenticationMissingException;
import sumcoda.boardbuddy.exception.auth.InvalidPasswordException;
import sumcoda.boardbuddy.exception.auth.InvalidUsernameException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (authentication == null) {
            throw new AuthenticationMissingException("인증 객체가 누락되었습니다. 관리자에게 문의하세요.");
        }

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new InvalidUsernameException("유효하지 않은 아이디 입니다. 올바른 아이디를 입력하였는지 확인해주세요.");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new InvalidPasswordException("유효하지 않은 비밀번호 입니다. 올바른 비밀번호를 입력하였는지 확인해주세요.");
        }

        return new CustomAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomAuthenticationToken.class);
    }
}
