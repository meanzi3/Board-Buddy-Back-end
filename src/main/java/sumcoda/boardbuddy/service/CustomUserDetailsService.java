package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sumcoda.boardbuddy.dto.AuthResponse;
import sumcoda.boardbuddy.dto.auth.CustomUserDetails;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthResponse.ProfileDTO member = memberRepository.findAuthDTOByUsername(username).orElseThrow(() ->
                new MemberNotFoundException("해당 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        if (member != null) {
            return new CustomUserDetails(member);
        }

        return null;
    }
}
