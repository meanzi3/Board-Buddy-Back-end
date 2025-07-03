package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sumcoda.boardbuddy.dto.GatherArticleRequest;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.enumerate.MessageType;
import sumcoda.boardbuddy.service.ChatMessageService;
import sumcoda.boardbuddy.service.ChatRoomService;
import sumcoda.boardbuddy.service.GatherArticleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sumcoda.boardbuddy.builder.ResponseBuilder.*;
import static sumcoda.boardbuddy.builder.ResponseBuilder.buildSuccessResponseWithMultiplePairKeyData;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GatherArticleController {

    private final GatherArticleService gatherArticleService;

    private final ChatRoomService chatRoomService;

    private final ChatMessageService chatMessageService;

    /**
     * @apiNote 현재는 사용률 저조로 기능이 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가 시 다시 활성화될 수 있음
     */
//    private final NotificationService notificationService;

    /**
     * 모집글 작성 요청
     *
     * @param createRequest     모집글 작성 요청 데이터
     * @param username          모집글 작성자 아이디
     * @return                  생성된 모집글과 관련된 응답 데이터
     */
    @PostMapping(value = "/api/gather-articles")
    public ResponseEntity<ApiResponse<Map<String, GatherArticleResponse.CreateDTO>>> createGatherArticle(
            @RequestBody GatherArticleRequest.CreateDTO createRequest,
            @RequestAttribute String username){

        GatherArticleResponse.CreateDTO createResponse = gatherArticleService.createGatherArticle(createRequest, username);

        Long gatherArticleId = createResponse.getId();

        // 채팅방 생성
        chatRoomService.createChatRoom(gatherArticleId);

        // 모집글 작성자 채팅방 입장
        Pair<Long, String> chatRoomAndNicknamePair = chatRoomService.enterChatRoom(gatherArticleId, username);

        // 채팅방 입장 메세지 전송
        chatMessageService.publishEnterOrExitChatMessage(chatRoomAndNicknamePair, MessageType.ENTER);

        /**
         * @apiNote 현재는 사용률 저조로 기능이 비활성화된 상태
         *          추후 사용자 요청 또는 트래픽 증가 시 다시 활성화될 수 있음
         */
//        notificationService.notifyGatherArticle(gatherArticleId, username);

        return buildSuccessResponseWithPairKeyData("post", createResponse, "모집글이 업로드 되었습니다", HttpStatus.CREATED);
    }

    /**
     * 모집글 조회 요청
     *
     * @param gatherArticleId 조회 요청 모집글 ID
     * @return 조회된 모집글과 관련된 응답 데이터
     */
    @GetMapping(value = "/api/gather-articles/{gatherArticleId}")
    public ResponseEntity<ApiResponse<Map<String, GatherArticleResponse.DetailedInfoDTO>>> getGatherArticle(
            @PathVariable Long gatherArticleId) {

        GatherArticleResponse.DetailedInfoDTO gatherArticleDetailedInfo = gatherArticleService.getGatherArticle(gatherArticleId);

        return buildSuccessResponseWithPairKeyData("post", gatherArticleDetailedInfo, "모집글 상세 정보를 성공적으로 조회 하였습니다.", HttpStatus.OK);
    }

    /**
     * 모집글 참가 신청 현황 조회
     *
     * @param gatherArticleId 조회 요청 모집글 ID
     * @param username 사용자 username
     * @return 모집글에 대한 요청을 보낸 사용자의 참가 신청 현황
     */
    @GetMapping(value = "/api/gather-articles/{gatherArticleId}/participation-status")
    public ResponseEntity<ApiResponse<Map<String, GatherArticleResponse.ParticipationApplicationStatusDTO>>> getParticipationApplicationStatus(
            @PathVariable Long gatherArticleId, @RequestAttribute String username) {

        GatherArticleResponse.ParticipationApplicationStatusDTO readResponse = gatherArticleService.getParticipationApplicationStatus(gatherArticleId, username);

        return buildSuccessResponseWithPairKeyData("post", readResponse, "모집글 참가 신청 현황을 성공적으로 조회 하였습니다.", HttpStatus.OK);
    }

    /**
     * 모집글 수정 요청
     *
     * @param gatherArticleId   수정 요청 모집글 id
     * @param updateRequest     모집글 수정 요청 데이터
     * @param username          사용자 username
     * @return                  수정된 모집글과 관련된 응답 데이터
     */
    @PutMapping(value = "/api/gather-articles/{gatherArticleId}")
    public ResponseEntity<ApiResponse<Map<String, GatherArticleResponse.UpdateDTO>>> updateGatherArticle(
            @PathVariable Long gatherArticleId,
            @RequestBody GatherArticleRequest.UpdateDTO updateRequest,
            @RequestAttribute String username) {
        GatherArticleResponse.UpdateDTO updateResponse = gatherArticleService.updateGatherArticle(gatherArticleId, updateRequest, username);
        return buildSuccessResponseWithPairKeyData("post", updateResponse, "모집글이 수정되었습니다.", HttpStatus.OK);
    }

    /**
     * 모집글 삭제 요청
     *
     * @param gatherArticleId   삭제 요청 모집글 id
     * @param username          사용자 username
     * @return                  삭제된 모집글과 관련된 응답 데이터
     */
    @DeleteMapping(value = "/api/gather-articles/{gatherArticleId}")
    public ResponseEntity<ApiResponse<Map<String, GatherArticleResponse.DeleteDTO>>> deleteGatherArticle(
            @PathVariable Long gatherArticleId,
            @RequestAttribute String username){
        GatherArticleResponse.DeleteDTO deleteResponse = gatherArticleService.deleteGatherArticle(gatherArticleId, username);
        return buildSuccessResponseWithPairKeyData("post", deleteResponse, "모집글이 삭제되었습니다.", HttpStatus.OK);
    }

    /**
     * 내가 작성한 모집글 조회 요청
     *
     * @param username 사용자 username
     * @return 내가 작성한 모집글 리스트
     **/
    @GetMapping(value = "/api/my/gather-articles")
    public ResponseEntity<ApiResponse<Map<String, List<GatherArticleResponse.GatherArticleInfosDTO>>>> getMyGatherArticles (
            @RequestAttribute String username) {
        log.info("get gather articles is working");

        List<GatherArticleResponse.GatherArticleInfosDTO> gatherArticles = gatherArticleService.getMyGatherArticles(username);

        return buildSuccessResponseWithPairKeyData("posts", gatherArticles, "작성한 모집글이 조회되었습니다.", HttpStatus.OK);
    }

    /**
     * 참가한 모집글 조회 요청
     *
     * @param username 사용자 username
     * @return 참가한 모집글 리스트
     **/
    @GetMapping(value = "/api/my/participations")
    public ResponseEntity<ApiResponse<Map<String, List<GatherArticleResponse.MyParticipationInfosDTO>>>> getMyParticipations (
            @RequestAttribute String username) {
        log.info("get my participations is working");

        List<GatherArticleResponse.MyParticipationInfosDTO> participations = gatherArticleService.getMyParticipations(username);

        return buildSuccessResponseWithPairKeyData("posts", participations, "참가한 모집글이 조회되었습니다.", HttpStatus.OK);
    }

    /**
     * @apiNote V1 - 내 동네 반경 n km 기반 모집글 리스트 조회 (현재 미사용, 향후 복원 가능)
     * 모집글 리스트 조회 요청
     *
     * @param page     페이지 번호
     * @param status   모집 상태 (옵션)
     * @param sort     정렬 기준 (옵션)
     * @param username 사용자 이름
     * @return 모집글 리스트
     */
//    @GetMapping("/api/gather-articles")
//    public ResponseEntity<ApiResponse<GatherArticleResponse.ReadListDTO>> getGatherArticles(
//            @RequestParam Integer page,
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) String sort,
//            @RequestAttribute String username) {
//        log.info("getGatherArticles is working");
//
//        /**
//         * @apiNote 임시 비활성화된 상태
//         *          위치 관련 코드 제거 필요
//         */
////        GatherArticleResponse.ReadListDTO posts = gatherArticleService.getGatherArticles(page, status, sort, username);
//
//        /**
//         * @apiNote 임시 작성된 상태
//         *          위치 관련 코드 수정후 제거 필요
//         */
//        GatherArticleResponse.ReadListDTO posts = new GatherArticleResponse.ReadListDTO();
//
//        return buildSuccessResponseWithMultiplePairKeyData(posts, "모집글 리스트 조회를 성공하였습니다.", HttpStatus.OK);
//    }

    /**
     * @apiNote V1 - 내 동네 반경 n km 기반 모집글 검색 (현재 미사용, 향후 복원 가능)
     * 모집글 검색 요청
     *
     * @param query     검색어
     * @param username  사용자 username
     * @return          검색 결과 리스트
     */
//    @GetMapping("/api/gather-articles/search")
//    public ResponseEntity<ApiResponse<Map<String, List<GatherArticleResponse.SearchResultDTO>>>> searchArticles(
//            @RequestParam String query,
//            @RequestAttribute String username) {
//
//        /**
//         * @apiNote 임시 비활성화된 상태
//         *          위치 관련 코드 제거 필요
//         */
////        List<GatherArticleResponse.SearchResultDTO> posts = gatherArticleService.searchArticles(query, username);
//
//        /**
//         * @apiNote 임시 작성된 상태
//         *          위치 관련 코드 수정후 제거 필요
//         */
//        List<GatherArticleResponse.SearchResultDTO> posts = new ArrayList<>();
//
//        return buildSuccessResponseWithPairKeyData("posts", posts, "모집글 검색에 성공하였습니다.", HttpStatus.OK);
//    }

    /**
     * @apiNote V2 - 사용자가 지정한 지역 기반 모집글 리스트 조회
     * 모집글 리스트 조회 요청 (검색 포함)
     *
     * @param page      페이지 번호
     * @param sido      시도
     * @param sgg       시군구
     * @param status    모집 상태 (옵션)
     * @param sort      정렬 기준 (옵션)
     * @param keyword   검색어
     * @return          모집글 리스트
     */
    @GetMapping("/api/gather-articles")
    public ResponseEntity<ApiResponse<GatherArticleResponse.ReadListDTO>> getGatherArticlesV2(
            @RequestParam Integer page,
            @RequestParam(required = false) String sido,
            @RequestParam(required = false) String sgg,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword) {

        log.info("getGatherArticlesV2 is working");
        log.info(keyword);

        GatherArticleResponse.ReadListDTO posts = gatherArticleService.getGatherArticlesV2(page, sido, sgg, status, sort, keyword);

        // 검색어가 있고, 결과가 비어 있는 경우 메시지만 다르게
        if (keyword != null && posts.getPosts().isEmpty()) {
            return buildSuccessResponseWithMultiplePairKeyData(
                    posts, "검색 결과가 없습니다.", HttpStatus.OK
            );
        }

        return buildSuccessResponseWithMultiplePairKeyData(
                posts, "모집글 리스트 조회를 성공하였습니다.", HttpStatus.OK
        );
    }
}

