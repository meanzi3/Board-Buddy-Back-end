package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sumcoda.boardbuddy.dto.AuthResponse;
import sumcoda.boardbuddy.dto.auth.CustomUserDetails;
import sumcoda.boardbuddy.exception.auth.InvalidUsernameException;
import sumcoda.boardbuddy.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthResponse.ProfileDTO member = memberRepository.findAuthDTOByUsername(username).orElseThrow(() ->
                new InvalidUsernameException("유효하지 않은 아이디 입니다. 올바른 아이디를 입력하였는지 확인해주세요."));

        if (member != null) {
            return new CustomUserDetails(member);
        }

        return null;
    }
}
