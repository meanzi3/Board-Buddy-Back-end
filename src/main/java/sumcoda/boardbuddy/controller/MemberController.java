package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sumcoda.boardbuddy.dto.MemberRequest;
import sumcoda.boardbuddy.dto.NearPublicDistrictResponse;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.MemberService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * 아이디 중복 확인 요청
     *
     * @param verifyUsernameDuplicationDTO 사용자가 입력한 아이디
     * @return 아이디가 중복되지 않았다면 true 반환, 아니라면 false 반환
     **/
    @PostMapping(value = "/api/auth/check-username")
    public ResponseEntity<Map<String, Object>> verifyUsernameDuplication(
            @RequestBody MemberRequest.VerifyUsernameDuplicationDTO verifyUsernameDuplicationDTO) {
        log.info("verify username duplication is working");

        Map<String, Object> response = new HashMap<>();

        Boolean isNotDuplicate = memberService.verifyUsernameDuplication(verifyUsernameDuplicationDTO);

        response.put("data", isNotDuplicate);

        response.put("message", "사용가능한 아이디 입니다.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 닉네임 중복 확인 요청
     *
     * @param verifyNicknameDuplicationDTO 사용자가 입력한 닉네임
     * @return 닉네임이 중복되지 않았다면 true 반환 아니라면 false 반환
     **/
    @PostMapping(value = "/api/auth/check-nickname")
    public ResponseEntity<Map<String, Object>> verifyNicknameDuplication(
            @RequestBody MemberRequest.VerifyNicknameDuplicationDTO verifyNicknameDuplicationDTO) {
        log.info("verify nickname duplication is working");

        Map<String, Object> response = new HashMap<>();

        Boolean isNotDuplicate = memberService.verifyNicknameDuplication(verifyNicknameDuplicationDTO);

        response.put("data", isNotDuplicate);

        response.put("message", "사용가능한 닉네임 입니다.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 회원가입 요청 캐치
     *
     * @param registerDTO 프론트로부터 전달받은 회원가입 정보
     * @return 회원가입에 성공했다면 해당 memberId 반환, 아니라면 null 반환
     **/
    @PostMapping(value = "/api/auth/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody MemberRequest.RegisterDTO registerDTO) {
        log.info("register is working");

        Map<String, Object> response = new HashMap<>();

        Long memberId = memberService.registerMember(registerDTO);

        response.put("data", memberId);

        response.put("message", "회원가입이 완료되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 신규 소셜 로그인 사용자의 회원강비 요청 캐치
     *
     * @param oAuth2RegisterDTO 프론트로부터 전달받은 소셜 로그인 유저 회원가입 정보
     * @return 첫 소셜 로그인 사용자가 회원가입에 성공했다면 memberId 반환, 아니라면 null 반환
     **/
    @PostMapping(value = "/api/auth/oauth2/register")
    public ResponseEntity<Map<String, Object>> oAuth2Register(@RequestBody MemberRequest.OAuth2RegisterDTO oAuth2RegisterDTO, Authentication authentication) {
        log.info("social register is working");

        Map<String, Object> response = new HashMap<>();

        Long memberId = memberService.registerOAuth2Member(oAuth2RegisterDTO, authentication);

        response.put("data", memberId);

        response.put("message", "회원가입이 완료되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 회원 탈퇴 요청 캐치
     *
     * @param authentication 로그인 정보를 포함하는 사용자 객체
     * @return 회원 탈퇴가 완료되었다면 해당 유저의 memberId 반환, 아니라면 null 반환
     **/
    @PostMapping(value = "/api/auth/withdrawal")
    public ResponseEntity<Map<String, Object>> withdrawalMember(Authentication authentication) {
        log.info("withdrawal member is working");

        Map<String, Object> response = new HashMap<>();

        Long memberId = memberService.withdrawalMember(authentication);

        response.put("data", memberId);

        response.put("message", "회원탈퇴가 완료되었습니다.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 멤버 위치 설정 요청 캐치
     *
     * @param locationDTO 사용자가 입력한 위치 정보
     **/
    @PostMapping("/api/locations")
    public ResponseEntity<ApiResponse<Map<Integer, List<NearPublicDistrictResponse.LocationDTO>>>> updateMemberLocation(
            @RequestBody MemberRequest.LocationDTO locationDTO,
            @ModelAttribute("username") String username) {
        log.info("updateMemberLocation is working");

        Map<Integer, List<NearPublicDistrictResponse.LocationDTO>> response = memberService.updateMemberLocation(locationDTO, username);

        return ResponseEntity.ok(new ApiResponse<>(response, "위치 정보 설정을 성공하였습니다."));
    }
}
