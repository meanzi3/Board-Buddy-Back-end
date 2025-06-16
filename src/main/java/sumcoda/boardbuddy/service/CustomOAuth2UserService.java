package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sumcoda.boardbuddy.dto.auth.oauth2.*;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.enumerate.MemberType;
import sumcoda.boardbuddy.enumerate.Role;
import sumcoda.boardbuddy.exception.auth.ClientRegistrationRetrievalException;
import sumcoda.boardbuddy.exception.auth.SocialUserInfoRetrievalException;
import sumcoda.boardbuddy.repository.member.MemberRepository;

import java.security.SecureRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 리소스 서버에서 유저 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        if (oAuth2User == null) {
            throw new SocialUserInfoRetrievalException("소셜 리소스 서버에서 유저 정보를 가져오지 못했습니다. 잠시후 다시 시도해주세요.");
        }

        log.info("oAuth2User = " + oAuth2User.getAttributes());

        // naver, google, kakao 등 값이 저장
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (registrationId == null) {
            throw new ClientRegistrationRetrievalException("소셜 리소스 서버에서 클라이언트의 정보를 가져오지 못했습니다. 잠시후 다시 시도해주세요.");
        }

        // 응답 받은 데이터 저장하기 위한 변수 생성
        OAuth2UserInfo oAuth2UserInfo;

        // 네이버에서 제공된 데이터라면
        switch (registrationId) {
            // 네이버에서 제공된 데이터라면
            case "naver":
                oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
                break;
            // 구글에서 제공된 데이터라면
            case "google":
                oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
                break;
            // 카카오에서 제공된 데이터라면
            case "kakao":
                oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
                break;
            default:
                return null;
        }

        String username = oAuth2UserInfo.getUsername();

        // 유저 이름을 바탕으로 DB 조회
        Member findMember = memberRepository.findByUsername(username).orElse(null);

        Role role = Role.USER;

        SecureRandom secureRandom = new SecureRandom();
        int randomNumber = 10000000 + secureRandom.nextInt(90000000);

        /**
         * @apiNote 현재는 사용률 저조로 해당 로직 비활성화된 상태
         *          추후 사용자 요청 또는 트래픽 증가시 다시 활성화될 수 있음
         */
        // 만약 신규 로그인 회원이라면
//        if (findMember == null) {
//            Member member = Member.buildMember(
//                    username,
//                    bCryptPasswordEncoder.encode(oAuth2UserInfo.getEmail()),
//                    oAuth2UserInfo.getName() + randomNumber,
//                    oAuth2UserInfo.getEmail(),
//                    null,
//                    null,
//                    null,
//                    null,
//                    2,
//                    50.0,
//                    0,
//                    0,
//                    0,
//                    0,
//                    0,
//                    0,
//                    0,
//                    0,
//                    0,
//                    null,
//                    null,
//                    0.0,
//                    MemberType.SOCIAL,
//                    Role.USER,
//                    null
//            );

            /**
             * @apiNote 임시로 활성화된 멤버 생성 로직
             *          앱 사용률 증가시 비활성화후에 기존 로직 활성화 예정
             */
            // 만약 신규 로그인 회원이라면
            if (findMember == null) {
                Member member = Member.buildMember(
                        username,
                        bCryptPasswordEncoder.encode(oAuth2UserInfo.getEmail()),
                        oAuth2UserInfo.getName() + randomNumber,
                        oAuth2UserInfo.getEmail(),
                        null,
                        50.0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        null,
                        null,
                        0.0,
                        MemberType.SOCIAL,
                        Role.USER,
                        null
                );

            memberRepository.save(member);

            // 신규 로그인 회원이 아니라면 현재 로그인한 유저 정보를 바탕으로 DB에 업데이트
        } else {

            // 해당 유저가 특정 경로에 접근할때 인가 작업에 필요한 role 값 업데이트
            role = findMember.getRole();

            // Dirty Checking 으로 업데이트 값 자동으로 DB 반영
            findMember.assignEmail(oAuth2UserInfo.getEmail());
        }

        // OAuth2LoginAuthenticationProvider에 OAuth2User 를 전달하고 로그인 완료
        return new CustomOAuth2User(oAuth2UserInfo, role);
    }
}
