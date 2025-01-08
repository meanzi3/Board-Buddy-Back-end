package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sumcoda.boardbuddy.dto.ReviewRequest;
import sumcoda.boardbuddy.dto.ReviewResponse;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.ReviewService;

import java.util.List;
import java.util.Map;

import static sumcoda.boardbuddy.builder.ResponseBuilder.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 전송 가능한 유저 리스트 조회 요청 캐치
     *
     * @param gatherArticleId 모집글 Id
     * @param username        로그인 사용자 아이디
     * @return 유저 리스트 조회가 성공했다면 약속된 SuccessResponse 반환
     **/
    @GetMapping("/api/v1/reviews/{gatherArticleId}")
    public ResponseEntity<ApiResponse<Map<String, List<ReviewResponse.UserDTO>>>> getParticipatedList (
            @PathVariable Long gatherArticleId,
            @RequestAttribute String username) {
        log.info("get participated list is working");

        List<ReviewResponse.UserDTO> participatedList = reviewService.getParticipatedList(gatherArticleId, username);

        return buildSuccessResponseWithPairKeyData("users", participatedList, "참가한 유저 리스트 조회에 성공하였습니다.", HttpStatus.OK);
    }

    /**
     * 리뷰 보내기 요청 캐치
     *
     * @param gatherArticleId 모집글 Id
     * @param sendDTO 리뷰를 받는 유저 닉네임과 리뷰 타입을 담은 dto
     * @param username 로그인 사용자 아이디
     * @return 리뷰 보내기가 성공했다면 약속된 SuccessResponse 반환
     **/
    @PostMapping("/api/v1/reviews/{gatherArticleId}")
    public ResponseEntity<ApiResponse<Void>> sendReview (
            @PathVariable Long gatherArticleId,
            @RequestBody ReviewRequest.SendDTO sendDTO,
            @RequestAttribute String username) {
        log.info("send Review is working");

        reviewService.sendReview(gatherArticleId, sendDTO, username);

        return buildSuccessResponseWithoutData("후기가 전송되었습니다.", HttpStatus.OK);
    }
}
