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
import sumcoda.boardbuddy.service.NotificationService;

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

    private final NotificationService notificationService;

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

        notificationService.notifyGatherArticle(gatherArticleId, username);

        return buildSuccessResponseWithPairKeyData("post", createResponse, "모집글이 업로드 되었습니다", HttpStatus.CREATED);
    }

    /**
     * 모집글 조회 요청
     *
     * @param gatherArticleId   조회 요청 모집글 id
     * @param username          사용자 username
     * @return                  조회된 모집글과 관련된 응답 데이터
     */
    @GetMapping(value = "/api/gather-articles/{gatherArticleId}")
    public ResponseEntity<ApiResponse<Map<String, GatherArticleResponse.ReadDTO>>> getGatherArticle(
            @PathVariable Long gatherArticleId,
            @RequestAttribute String username) {
        GatherArticleResponse.ReadDTO readResponse = gatherArticleService.getGatherArticle(gatherArticleId, username);
        return buildSuccessResponseWithPairKeyData("post", readResponse, "성공적으로 조회되었습니다.", HttpStatus.OK);
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
     * 모집글 리스트 조회 요청
     *
     * @param page     페이지 번호
     * @param status   모집 상태 (옵션)
     * @param sort     정렬 기준 (옵션)
     * @param username 사용자 이름
     * @return 모집글 리스트
     */
    @GetMapping("/api/gather-articles")
    public ResponseEntity<ApiResponse<GatherArticleResponse.ReadListDTO>> getGatherArticles(
            @RequestParam Integer page,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sort,
            @RequestAttribute String username) {
        log.info("getGatherArticles is working");

        GatherArticleResponse.ReadListDTO posts = gatherArticleService.getGatherArticles(page, status, sort, username);

        return buildSuccessResponseWithMultiplePairKeyData(posts, "모집글 리스트 조회를 성공하였습니다.", HttpStatus.OK);
    }

    /**
     * 모집글 검색 요청
     *
     * @param query     검색어
     * @param username  사용자 username
     * @return          검색 결과 리스트
     */
    @GetMapping("/api/gather-articles/search")
    public ResponseEntity<ApiResponse<Map<String, List<GatherArticleResponse.SearchResultDTO>>>> searchArticles(
            @RequestParam String query,
            @RequestAttribute String username) {

        List<GatherArticleResponse.SearchResultDTO> posts = gatherArticleService.searchArticles(query, username);

        return buildSuccessResponseWithPairKeyData("posts", posts, "모집글 검색에 성공하였습니다.", HttpStatus.OK);
    }
}

