package sumcoda.boardbuddy.mapper;

import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.client.ChatMessageLastSentInfoDTO;
import sumcoda.boardbuddy.dto.client.ChatRoomInfoDTO;
import sumcoda.boardbuddy.dto.client.GatherArticleSimpleInfoDTO;
import sumcoda.boardbuddy.dto.fetch.ChatMessageLastSentInfoProjection;
import sumcoda.boardbuddy.dto.fetch.ChatRoomInfoProjection;
import sumcoda.boardbuddy.dto.fetch.GatherArticleSimpleInfoProjection;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class ChatRoomMapper {

    /**
     * ChatRoomInfoProjection 리스트를 ChatRoomInfoDTO 리스트로 매핑
     *
     * @param projections 조회된 ChatRoomInfoProjection 리스트
     * @return 변환된 ChatRoomInfoDTO 객체들의 리스트
     */
    public List<ChatRoomInfoDTO> toChatRoomInfoDTOList(List<ChatRoomInfoProjection> projections) {
        return projections.stream().map(chatRoomDetailsProjection -> {

                    GatherArticleSimpleInfoProjection gatherArticleProjection = chatRoomDetailsProjection.gatherArticleSimpleInfoProjection();

                    GatherArticleSimpleInfoDTO gatherArticleSimpleInfo = GatherArticleSimpleInfoDTO.builder()
                            .gatherArticleId(gatherArticleProjection.gatherArticleId())
                            .title(gatherArticleProjection.title())
                            .meetingLocation(gatherArticleProjection.meetingLocation())
                            .currentParticipants(gatherArticleProjection.currentParticipants())
                            .build();

                    ChatMessageLastSentInfoProjection chatMessageProjection = chatRoomDetailsProjection.chatMessageLastSentInfoProjection();

                    ChatMessageLastSentInfoDTO latestChatMessageInfo = ChatMessageLastSentInfoDTO.builder()
                            .content(chatMessageProjection.content())
                            .sentAt(LocalDateTime.ofInstant(chatMessageProjection.sentAt(), ZoneId.systemDefault()))
                            .build();

                    return ChatRoomInfoDTO.builder()
                            .chatRoomId(chatRoomDetailsProjection.chatRoomId())
                            .gatherArticleSimpleInfo(gatherArticleSimpleInfo)
                            .latestChatMessageInfo(latestChatMessageInfo)
                            .build();
                })
                .toList();
    }
}
