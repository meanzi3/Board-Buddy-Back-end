package sumcoda.boardbuddy.util;

import sumcoda.boardbuddy.dto.ChatMessageResponse;
import sumcoda.boardbuddy.dto.ChatRoomResponse;
import sumcoda.boardbuddy.dto.GatherArticleResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class ChatRoomUtil {

    public static List<ChatRoomResponse.ChatRoomDetailsDTO> convertToChatRoomDetailsDTO(List<ChatRoomResponse.ChatRoomDetailsProjectionDTO> chatRoomDetailsListByUsername) {
        return chatRoomDetailsListByUsername.stream().map(chatRoomDetailsProjection -> {

                    GatherArticleResponse.SimpleInfoProjectionDTO gatherArticleSimpleProjectionInfo = chatRoomDetailsProjection.getGatherArticleSimpleProjectionInfo();

                    GatherArticleResponse.SimpleInfoDTO gatherArticleSimpleInfo = GatherArticleResponse.SimpleInfoDTO.builder()
                            .gatherArticleId(gatherArticleSimpleProjectionInfo.getGatherArticleId())
                            .title(gatherArticleSimpleProjectionInfo.getTitle())
                            .meetingLocation(gatherArticleSimpleProjectionInfo.getMeetingLocation())
                            .currentParticipants(gatherArticleSimpleProjectionInfo.getCurrentParticipants())
                            .build();

                    ChatMessageResponse.LatestChatMessageInfoProjectionDTO latestChatMessageInfoProjection = chatRoomDetailsProjection.getLatestChatMessageInfoProjectionDTO();

                    ChatMessageResponse.LatestChatMessageInfoDTO latestChatMessageInfo = ChatMessageResponse.LatestChatMessageInfoDTO.builder()
                            .content(latestChatMessageInfoProjection.getContent())
                            .sentAt(LocalDateTime.ofInstant(latestChatMessageInfoProjection.getSentAt(), ZoneId.systemDefault()))
                            .build();

                    return ChatRoomResponse.ChatRoomDetailsDTO.builder()
                            .chatRoomId(chatRoomDetailsProjection.getChatRoomId())
                            .gatherArticleSimpleInfo(gatherArticleSimpleInfo)
                            .latestChatMessageInfo(latestChatMessageInfo)
                            .build();
                })
                .toList();
    }
}
