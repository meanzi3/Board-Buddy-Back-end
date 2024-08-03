package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import sumcoda.boardbuddy.dto.ChatMessageRequest;
import sumcoda.boardbuddy.dto.ChatMessageResponse;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.service.ChatMessageService;

import java.util.List;
import java.util.Map;

import static sumcoda.boardbuddy.builder.ResponseBuilder.buildSuccessResponseWithPairKeyData;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * 특정 채팅방에 메세지 발행 및 전송
     *
     * @param chatRoomId 채팅방 Id
     * @param publishDTO 발행할 메세지 내용 DTO
//     * @param username 메시지를 발행하는 사용자 이름
     **/
    @MessageMapping("/{chatRoomId}")
    public void publishMessage(
            @DestinationVariable Long chatRoomId,
            @Payload ChatMessageRequest.PublishDTO publishDTO) {

        chatMessageService.publishMessage(chatRoomId, publishDTO);
    }

    /**
     * 채팅방 메세지 내역 조회
     *
     * @param chatRoomId 채팅방 Id
     * @param username 요청을 보낸 사용자 아이디
     * @return 채팅방 메세지 내역
     */
    @GetMapping("/api/chat/rooms/{chatRoomId}/messages")
    public ResponseEntity<ApiResponse<Map<String, List<ChatMessageResponse.ChatMessageInfoDTO>>>> getChatMessages(
            @PathVariable Long chatRoomId,
            @RequestAttribute String username) {

        List<ChatMessageResponse.ChatMessageInfoDTO> chatMessages = chatMessageService.findMessagesAfterMemberJoinedByChatRoomIdAndUsername(chatRoomId, username);

        return buildSuccessResponseWithPairKeyData("chatMessages", chatMessages, "채팅 메세지들의 정보를 성공적으로 조회했습니다.", HttpStatus.OK);
    }
}
