package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import sumcoda.boardbuddy.dto.MemberRequest;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.ReviewService;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildSuccessResponseWithoutData;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 보내기 요청 캐치
     *
     * @param gatherArticleId 모집글 Id
     * @param reviewDTO 리뷰를 받는 유저 닉네임과 리뷰 타입을 담은 dto
     * @param username 로그인 사용자 아이디
     * @return 리뷰 보내기가 성공했다면 약속된 SuccessResponse 반환
     **/
    @PostMapping("/api/reviews/{gatherArticleId}")
    public ResponseEntity<ApiResponse<Void>> sendReview (
            @PathVariable Long gatherArticleId,
            @RequestBody MemberRequest.ReviewDTO reviewDTO,
            @RequestAttribute String username) {
        log.info("send Review is working");

        reviewService.sendReview(gatherArticleId, reviewDTO, username);

        return buildSuccessResponseWithoutData("후기가 전송되었습니다.", HttpStatus.OK);
    }
}
