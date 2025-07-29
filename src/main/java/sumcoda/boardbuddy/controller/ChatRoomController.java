package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.dto.client.ChatRoomInfoDTO;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.ChatRoomService;
import sumcoda.boardbuddy.service.GatherArticleService;

import java.util.List;
import java.util.Map;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildSuccessResponseWithPairKeyData;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    private final GatherArticleService gatherArticleService;

    /**
     * 채팅방 정보와 연관된 모집글 정보 조회
     *
     * @param chatRoomId      채팅방 Id
     * @param gatherArticleId 모집글 Id
     * @return 채팅방과 연관된 모집글 정보
     **/
    @GetMapping("/api/chat/rooms/{chatRoomId}/gather-articles/{gatherArticleId}")
    public ResponseEntity<ApiResponse<Map<String, GatherArticleResponse.SummaryInfoDTO>>> getChatRoomGatherArticleInfo(@PathVariable Long chatRoomId,
                                                                                                                       @PathVariable Long gatherArticleId,
                                                                                                                       @RequestAttribute String username) {

        GatherArticleResponse.SummaryInfoDTO gatherArticleSimpleInfo = gatherArticleService.getChatRoomGatherArticleSimpleInfo(chatRoomId, gatherArticleId, username);

        return buildSuccessResponseWithPairKeyData("gatherArticleSimpleInfo", gatherArticleSimpleInfo, "모집글 정보를 성공적으로 조회했습니다.", HttpStatus.OK);
    }

    /**
     * 특정 사용자가 참여한 채팅방 목록 조회
     *
     * @param username 조회하려는 사용자의 아이디
     * @return 사용자가 참여한 채팅방 목록
     */
    @GetMapping("/api/chat/rooms")
    public ResponseEntity<ApiResponse<Map<String, List<ChatRoomInfoDTO>>>> getChatRoomDetailsByUsername(@RequestAttribute String username) {
        List<ChatRoomInfoDTO> chatRoomDetailsList = chatRoomService.getChatRoomDetailsListByUsername(username);

        return buildSuccessResponseWithPairKeyData("chatRoomDetailsList", chatRoomDetailsList, "참여중인 채팅방 목록을 성공적으로 조회했습니다.", HttpStatus.OK);
    }
}
