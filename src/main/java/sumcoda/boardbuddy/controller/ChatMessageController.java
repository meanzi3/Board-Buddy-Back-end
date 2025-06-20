package sumcoda.boardbuddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import sumcoda.boardbuddy.dto.ChatMessageRequest;
import sumcoda.boardbuddy.dto.ChatMessageResponse;
import sumcoda.boardbuddy.dto.common.ApiResponse;
import sumcoda.boardbuddy.dto.common.PageResponse;
import sumcoda.boardbuddy.service.ChatMessageService;


import static sumcoda.boardbuddy.builder.ResponseBuilder.buildSuccessResponseWithMultiplePairKeyData;

@Slf4j
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

//    /**
//     * 채팅방 메세지 내역 조회
//     *
//     * @param chatRoomId 채팅방 Id
//     * @param username 요청을 보낸 사용자 아이디
//     * @return 채팅방 메세지 내역
//     * @version 1.0
//     */
//    @GetMapping("/api/chat/rooms/{chatRoomId}/messages")
//    public ResponseEntity<ApiResponse<Map<String, List<ChatMessageResponse.ChatMessageItemInfoDTO>>>> getChatMessages(
//            @PathVariable Long chatRoomId,
//            @RequestAttribute String username) {
//
//        List<ChatMessageResponse.ChatMessageItemInfoDTO> chatMessages = chatMessageService.findChatMessagesAfterMemberJoinedAt(chatRoomId, username);
//
//        return buildSuccessResponseWithPairKeyData("chatMessages", chatMessages, "채팅 메세지들의 정보를 성공적으로 조회했습니다.", HttpStatus.OK);
//    }

    /**
     * 채팅방 메세지 내역 조회
     *
     * @param chatRoomId 채팅방 Id
     * @param username   요청을 보낸 사용자 아이디
     * @return 채팅방 메세지 내역
     * @version 2.0
     * @since 1.0
     */
    @GetMapping("/api/chat/{chatRoomId}/messages")
    public ResponseEntity<ApiResponse<PageResponse<ChatMessageResponse.ChatMessageItemInfoDTO>>> getChatMessages(
            @PathVariable Long chatRoomId,
            @RequestAttribute String username,
            @RequestParam(name = "direction", defaultValue = "initial") String direction,
            @RequestParam(required = false) String cursor) {

        PageResponse<ChatMessageResponse.ChatMessageItemInfoDTO> chatMessages = switch (direction) {
            case "newer" -> chatMessageService.findNewerChatMessages(chatRoomId, username, cursor);

            case "older" -> chatMessageService.findOlderChatMessages(chatRoomId, username, cursor);

            default -> chatMessageService.findInitialChatMessages(chatRoomId, username);
        };

        return buildSuccessResponseWithMultiplePairKeyData(chatMessages, "채팅 메세지들의 정보를 성공적으로 조회했습니다.", HttpStatus.OK);
    }

//    /**
//     * 채팅방 메세지 내역 조회
//     *
//     * @param chatRoomId 채팅방 Id
//     * @param username   요청을 보낸 사용자 아이디
//     * @return 채팅방 메세지 내역
//     * @version 2.0
//     * @since 1.0
//     */
//    @GetMapping("/api/chat/rooms/{chatRoomId}/messages/test")
//    public ResponseEntity<ApiResponse<PageResponse<ChatMessageResponse.ChatMessageItemInfoDTO>>> getChatMessagesTest(
//            @PathVariable Long chatRoomId,
//            @RequestParam String username,
//            @RequestParam(name = "direction", defaultValue = "initial") String direction,
//            @RequestParam(required = false) String cursor) throws JsonProcessingException {
//
//        PageResponse<ChatMessageResponse.ChatMessageItemInfoDTO> chatMessages = switch (direction) {
//            case "newer" -> chatMessageService.findNewerChatMessages(chatRoomId, username, cursor);
//
//            case "older" -> chatMessageService.findOlderChatMessages(chatRoomId, username, cursor);
//
//            default -> chatMessageService.findInitialChatMessages(chatRoomId, username);
//        };
//
//        return buildSuccessResponseWithMultiplePairKeyData(chatMessages, "채팅 메세지들의 정보를 성공적으로 조회했습니다.", HttpStatus.OK);
//    }


}