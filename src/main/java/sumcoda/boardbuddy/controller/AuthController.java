package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import sumcoda.boardbuddy.dto.AuthRequest;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.AuthService;

import java.util.Map;

import static sumcoda.boardbuddy.builder.ResponseBuilder.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * SMS 인증 메시지를 사용자에게 보내는 요청 캐치
     *
     * @param sendSMSCertificationDTO 로그인 정보를 포함하는 사용자 객체
     * @return 인증번호가 성공적으로 전달되었다면 약속된 SuccessResponse 반환
     **/
    @PostMapping("/api/auth/sms-certifications/send")
    public ResponseEntity<ApiResponse<Void>> sendSMS(@RequestBody AuthRequest.SendSMSCertificationDTO sendSMSCertificationDTO) {
        log.info("send sms is working");

        authService.sendSMS(sendSMSCertificationDTO);

        return buildSuccessResponseWithoutData("입력하신 SMS 인증 번호가 일치합니다.", HttpStatus.OK);
    }

    /**
     *  SMS 인증번호 확인 요청 캐치
     *
     * @param validateSMSCertificationDTO 인증번호 확인을 요청한 사용자의 핸드폰 번호가 저장되어있는 DTO
     * @return 인증 번호가 올바르게 입력되었다면 약속된 SuccessResponse 반환
     **/
    @PostMapping("/api/auth/sms-certifications/verify")
    public ResponseEntity<ApiResponse<Void>> verifyCertificationNumber(@RequestBody AuthRequest.ValidateSMSCertificationDTO validateSMSCertificationDTO) {
        log.info("validate certification is working");

        authService.validateCertificationNumber(validateSMSCertificationDTO);

        return buildSuccessResponseWithoutData("입력하신 SMS 인증 번호가 일치합니다.", HttpStatus.OK);
    }

    /**
     * 사용자의 로그인 상태를 확인
     *
     * @param username 로그인 사용자 아이디
     * @return 사용자가 로그인한 상태라면 해당 사용자의 프로필을 기반으로한 약속된 SuccessResponse 반환
     **/
    @GetMapping("/api/auth/status")
    public ResponseEntity<ApiResponse<Map<String, MemberResponse.ProfileDTO>>> isAuthenticated(@RequestAttribute String username) {
        log.info("check session is working");

        MemberResponse.ProfileDTO profileDTO = authService.isAuthenticated(username);

        return buildSuccessResponseWithPairKeyData("profileDTO", profileDTO, "유효한 세션입니다.", HttpStatus.OK);
    }

    /**
     * 사용자의 비밀번호 검증 요청 캐치
     *
     * @param validatePasswordDTO 검증하려는 비밀번호가 저장되어있는 DTO
     * @param username 로그인 사용자 아이디
     * @return 사용자가 비밀번호 검증에 성공했다면 약속된 SuccessResponse 반환
     **/
    @PostMapping("/api/auth/password")
    public ResponseEntity<ApiResponse<Void>> validatePassword(
            @RequestBody AuthRequest.ValidatePasswordDTO validatePasswordDTO,
            @RequestAttribute String username) {

        log.info("validate password is working");

        authService.validatePassword(validatePasswordDTO, username);

        return buildSuccessResponseWithoutData("비밀번호 검증이 완료되었습니다.", HttpStatus.OK);
    }
}
