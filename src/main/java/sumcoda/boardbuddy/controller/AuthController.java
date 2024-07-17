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
import sumcoda.boardbuddy.service.AuthService;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> sendSMS(@RequestBody AuthRequest.SendSMSCertificationDTO sendSMSCertificationDTO) {
        log.info("send sms is working");

        Map<String, Object> response = new HashMap<>();

        authService.sendSMS(sendSMSCertificationDTO);

        response.put("data", true);

        response.put("message", "SMS 인증 번호가 성공적으로 발송되었습니다.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     *  SMS 인증번호 확인 요청 캐치
     *
     * @param validateSMSCertificationDTO 인증번호 확인을 요청한 사용자의 핸드폰 번호가 저장되어있는 DTO
     * @return 인증 번호가 올바르게 입력되었다면 true 아니면 false 반환
     **/
    @PostMapping("/api/auth/sms-certifications/verify")
    public ResponseEntity<Map<String, Object>> verifyCertificationNumber(@RequestBody AuthRequest.ValidateSMSCertificationDTO validateSMSCertificationDTO) {
        log.info("validate certification is working");

        Map<String, Object> response = new HashMap<>();

        authService.validateCertificationNumber(validateSMSCertificationDTO);

        response.put("data", true);

        response.put("message", "입력하신 SMS 인증 번호가 일치합니다.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 사용자의 로그인 상태를 확인
     *
     * @param authentication 로그인 정보를 포함하는 사용자 객체
     * @return 사용자가 로그인한 상태라면 해당 사용자의 프로필 반환 아니라면 null 반환
     **/
    @GetMapping("/api/auth/status")
    public ResponseEntity<?> isAuthenticated(Authentication authentication) {

        Map<String, Object> responseData = new HashMap<>();

        MemberResponse.ProfileDTO profileDTO = authService.isAuthenticated(authentication);

        responseData.put("data", profileDTO);

        responseData.put("message", "유효한 세션입니다.");

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
