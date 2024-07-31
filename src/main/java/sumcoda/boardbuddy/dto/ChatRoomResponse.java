package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.MemberChatRoomRole;

import java.time.LocalDateTime;

public class ChatRoomResponse {

    @Getter
    @NoArgsConstructor
    public static class InfoDTO {

        private Long id;

        private LocalDateTime joinedAt;

        private MemberChatRoomRole memberChatRoomRole;

        @Builder
        public InfoDTO(Long id, LocalDateTime joinedAt, MemberChatRoomRole memberChatRoomRole) {
            this.id = id;
            this.joinedAt = joinedAt;
            this.memberChatRoomRole = memberChatRoomRole;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ValidateDTO {

        private Long id;

        @Builder
        public ValidateDTO(Long id) {
            this.id = id;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ChatRoomDetailsDTO {

        private Long chatRoomId;

        private GatherArticleResponse.SimpleInfoDTO gatherArticleSimpleInfo;

        private ChatMessageResponse.LastChatMessageInfoDTO lastChatMessageInfo;

        @Builder
        public ChatRoomDetailsDTO(Long chatRoomId, GatherArticleResponse.SimpleInfoDTO gatherArticleSimpleInfo, ChatMessageResponse.LastChatMessageInfoDTO lastChatMessageInfo) {
            this.chatRoomId = chatRoomId;
            this.gatherArticleSimpleInfo = gatherArticleSimpleInfo;
            this.lastChatMessageInfo = lastChatMessageInfo;
        }
    }

}
