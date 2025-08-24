package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sumcoda.boardbuddy.dto.client.MemberDetailDTO;
import sumcoda.boardbuddy.dto.MemberRequest;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.ProfileService;

import java.util.Map;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildSuccessResponseWithPairKeyData;
import static sumcoda.boardbuddy.builder.ResponseBuilder.buildSuccessResponseWithoutData;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 프로필 조회 요청 캐치
     *
     * @param nickname 유저 닉네임
     * @return 프로필 조회가 성공했다면 약속된 SuccessResponse 반환
     **/
    @GetMapping("/api/profiles/{nickname}")
    public ResponseEntity<ApiResponse<Map<String, MemberDetailDTO>>> getMemberProfileByNickname(@PathVariable String nickname) {
        log.info("get member profile is working");

        MemberDetailDTO profileInfoDTO = profileService.getMemberProfileByNickname(nickname);

        return buildSuccessResponseWithPairKeyData("profile", profileInfoDTO, "프로필이 조회되었습니다.", HttpStatus.OK);
    }

    /**
     * 프로필 수정 요청 캐치
     *
     * @param username         유저 아이디
     * @param updateProfileDTO 수정할 정보가 담겨있는 DTO
     * @param profileImageFile 수정할 프로필 이미지 파일
     * @return 프로필 조회가 성공했다면 약속된 SuccessResponse 반환
     **/
    @PutMapping("/api/profiles")
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @RequestAttribute String username,
            @RequestPart(value = "UpdateProfileDTO") MemberRequest.UpdateProfileDTO updateProfileDTO,
            @RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile) {
        log.info("update Profile is working");

        profileService.updateProfile(username, updateProfileDTO, profileImageFile);

        return buildSuccessResponseWithoutData("프로필이 수정되었습니다.", HttpStatus.OK);
    }
}
