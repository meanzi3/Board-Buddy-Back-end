package sumcoda.boardbuddy.dto.client;

import lombok.Builder;

@Builder
public record ChatRoomInfoDTO(

        Long chatRoomId,

        GatherArticleSimpleInfoDTO gatherArticleSimpleInfo,

        ChatMessageLastSentInfoDTO latestChatMessageInfo
) {
}
