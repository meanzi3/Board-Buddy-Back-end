package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sumcoda.boardbuddy.dto.MemberRequest;
import sumcoda.boardbuddy.service.MemberService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 요청 캐치
     *
     * @param registerDTO 프론트로부터 전달받은 회원가입 정보
     **/
    @PostMapping(value = "/api/auth/register")
    public ResponseEntity<?> register(@RequestBody MemberRequest.RegisterDTO registerDTO) {
        log.info("register is working");

        Map<String, Object> response = new HashMap<>();

        Long memberId = memberService.registerMember(registerDTO);

        response.put("data", memberId);

        response.put("message", "회원가입이 완료되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
