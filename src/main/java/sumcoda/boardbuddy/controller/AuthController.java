package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sumcoda.boardbuddy.dto.AuthRequest;
import sumcoda.boardbuddy.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/auth/sms-certifications/send")
    public ResponseEntity<Map<String, Object>> sendSMS(@RequestBody AuthRequest.SendSMSCertificationDTO requestDto) {
        log.info("send sms is working");

        Map<String, Object> response = new HashMap<>();

        authService.sendSMS(requestDto);

        response.put("data", true);

        response.put("message", "SMS 인증 번호가 성공적으로 발송되었습니다.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //인증번호 확인
    @PostMapping("/api/auth/sms-certifications/verify")
    public ResponseEntity<Map<String, Object>> verifyCertificationNumber(@RequestBody AuthRequest.ValidateSMSCertificationDTO requestDto) {
        log.info("validate certification is working");

        Map<String, Object> response = new HashMap<>();

        authService.validateCertificationNumber(requestDto);

        response.put("data", true);

        response.put("message", "입력하신 SMS 인증 번호가 일치합니다.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
