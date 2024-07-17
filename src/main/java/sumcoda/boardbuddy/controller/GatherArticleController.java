package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.service.GatherArticleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GatherArticleController {

    private final GatherArticleService gatherArticleService;

    /**
     * 내가 작성한 모집글 조회 요청
     *
     * @param userDetails 현재 인증된 사용자 정보
     * @return 내가 작성한 모집글 리스트
     **/
    @GetMapping(value = "/api/my/gatherArticles")
    public ResponseEntity<?> getMyGatherArticles (@AuthenticationPrincipal UserDetails userDetails) {
        log.info("get gather articles is working");

        Map<String, Object> response = new HashMap<>();

        String username = userDetails.getUsername();

        List<GatherArticleResponse.GatherArticleInfosDTO> gatherArticles = gatherArticleService.getMyGatherArticles(username);

        response.put("data", Map.of("posts", gatherArticles));

        response.put("message", "내 모집글이 성공적으로 조회되었습니다.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

