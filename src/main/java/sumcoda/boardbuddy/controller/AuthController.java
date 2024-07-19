package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sumcoda.boardbuddy.dto.AuthRequest;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.AuthService;

import java.util.HashMap;
import java.util.Map;

import static sumcoda.boardbuddy.util.ResponseHandlerUtil.buildSuccessResponse;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * SMS 인증 메시지를 사용자에게 보내는 요청 캐치
     *
     * @param sendSMSCertificationDTO 로그인 정보를 포함하는 사용자 객체
     * @return 인증번호가 성공적으로 전달되었다면 true 아니라면 false 반환
     **/
    @PostMapping("/api/auth/sms-certifications/send")
    public ResponseEntity<ApiResponse<Object>> sendSMS(@RequestBody AuthRequest.SendSMSCertificationDTO sendSMSCertificationDTO) {
        log.info("send sms is working");

        Map<String, Object> response = new HashMap<>();

        authService.sendSMS(sendSMSCertificationDTO);

        return buildSuccessResponse(null, "입력하신 SMS 인증 번호가 일치합니다.", HttpStatus.OK);
    }

    /**
     *  SMS 인증번호 확인 요청 캐치
     *
     * @param validateSMSCertificationDTO 인증번호 확인을 요청한 사용자의 핸드폰 번호가 저장되어있는 DTO
     * @return 인증 번호가 올바르게 입력되었다면 true 아니면 false 반환
     **/
    @PostMapping("/api/auth/sms-certifications/verify")
    public ResponseEntity<ApiResponse<Object>> verifyCertificationNumber(@RequestBody AuthRequest.ValidateSMSCertificationDTO validateSMSCertificationDTO) {
        log.info("validate certification is working");

        authService.validateCertificationNumber(validateSMSCertificationDTO);

        return buildSuccessResponse(null, "입력하신 SMS 인증 번호가 일치합니다.", HttpStatus.OK);
    }

    /**
     * 사용자의 로그인 상태를 확인
     *
     * @param authentication 로그인 정보를 포함하는 사용자 객체
     * @return 사용자가 로그인한 상태라면 해당 사용자의 프로필 반환 아니라면 null 반환
     **/
    @GetMapping("/api/auth/status")
    public ResponseEntity<ApiResponse<Object>> isAuthenticated(Authentication authentication) {
        log.info("check session is working");

        MemberResponse.ProfileDTO profileDTO = authService.isAuthenticated(authentication);

        return buildSuccessResponse(profileDTO, "유효한 세션입니다.", HttpStatus.OK);
    }

    /**
     * 사용자의 비밀번호 검증 요청 캐치
     *
     * @param authentication 로그인 정보를 포함하는 사용자 객체
     * @return 사용자가 비밀번호 검증에 성공했다면 true 반환, 아니라면 false 반환
     **/
    @PostMapping("/api/auth/password")
    public ResponseEntity<ApiResponse<Object>> validatePassword(
            @RequestBody AuthRequest.ValidatePasswordDTO validatePasswordDTO,
            Authentication authentication) {

        log.info("validate password is working");

        authService.validatePassword(validatePasswordDTO, authentication);

        return buildSuccessResponse(null, "비밀번호 검증이 완료되었습니다.", HttpStatus.OK);
    }
}
