package sumcoda.boardbuddy.util;

import sumcoda.boardbuddy.dto.ChatMessageResponse;
import sumcoda.boardbuddy.dto.ChatRoomResponse;
import sumcoda.boardbuddy.dto.GatherArticleResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class ChatRoomUtil {

    public static List<ChatRoomResponse.ChatRoomDetailsDTO> convertToChatRoomDetailsDTO(List<ChatRoomResponse.ChatRoomDetailsProjectionDTO> chatRoomDetailsListByUsername) {
        return chatRoomDetailsListByUsername.stream().map(chatRoomDetailsProjectionDTO -> {

                    GatherArticleResponse.SimpleInfoProjectionDTO gatherArticleSimpleProjectionInfo = chatRoomDetailsProjectionDTO.getGatherArticleSimpleProjectionInfo();

                    GatherArticleResponse.SimpleInfoDTO gatherArticleSimpleInfoDTO = GatherArticleResponse.SimpleInfoDTO.builder()
                            .gatherArticleId(gatherArticleSimpleProjectionInfo.getGatherArticleId())
                            .title(gatherArticleSimpleProjectionInfo.getTitle())
                            .meetingLocation(gatherArticleSimpleProjectionInfo.getMeetingLocation())
                            .currentParticipants(gatherArticleSimpleProjectionInfo.getCurrentParticipants())
                            .build();

                    ChatMessageResponse.LatestChatMessageInfoProjectionDTO latestChatMessageInfoProjectionDTO = chatRoomDetailsProjectionDTO.getLatestChatMessageInfoProjectionDTO();

                    ChatMessageResponse.LatestChatMessageInfoDTO latestChatMessageInfoDTO = ChatMessageResponse.LatestChatMessageInfoDTO.builder()
                            .content(latestChatMessageInfoProjectionDTO.getContent())
                            .sentAt(LocalDateTime.ofInstant(latestChatMessageInfoProjectionDTO.getSentAt(), ZoneId.systemDefault()))
                            .build();

                    return ChatRoomResponse.ChatRoomDetailsDTO.builder()
                            .chatRoomId(chatRoomDetailsProjectionDTO.getChatRoomId())
                            .gatherArticleSimpleInfo(gatherArticleSimpleInfoDTO)
                            .latestChatMessageInfoDTO(latestChatMessageInfoDTO)
                            .build();
                })
                .toList();
    }
}
