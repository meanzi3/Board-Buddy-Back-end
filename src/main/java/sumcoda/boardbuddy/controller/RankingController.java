package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sumcoda.boardbuddy.dto.client.MemberRankingDTO;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.RankingService;

import java.util.List;
import java.util.Map;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildSuccessResponseWithPairKeyData;

@RestController
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    /**
     * 랭킹 조회 요청 캐치
     *
     * @return TOP3 리스트를 조회하여 약속된 SuccessResponse 반환
     */
    @GetMapping("/api/rankings")
    public ResponseEntity<ApiResponse<Map<String, List<MemberRankingDTO>>>> getTop3Rankings() {
        List<MemberRankingDTO> memberRankingDTO = rankingService.getTop3Rankings();

        return buildSuccessResponseWithPairKeyData("rankings", memberRankingDTO,"랭킹 조회에 성공했습니다.", HttpStatus.OK);
    }
}
