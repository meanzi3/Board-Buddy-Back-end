package sumcoda.boardbuddy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.MessageType;

import java.time.LocalDateTime;

public class ChatMessageResponse {

    @Getter
    @NoArgsConstructor
    public static class ChatMessageInfoDTO {

        private String content;

        private String nickname;

        private String profileImageS3SavedURL;

        private Integer rank;

        private MessageType messageType;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime sentAt;

        @Builder
        public ChatMessageInfoDTO(String content, String nickname, String profileImageS3SavedURL, Integer rank, MessageType messageType, LocalDateTime sentAt) {
            this.content = content;
            this.nickname = nickname;
            this.profileImageS3SavedURL = profileImageS3SavedURL;
            this.rank = rank;
            this.messageType = messageType;
            this.sentAt = sentAt;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class EnterOrExitMessageInfoDTO {

        private String content;

        private MessageType messageType;

        @Builder
        public EnterOrExitMessageInfoDTO(String content, MessageType messageType) {
            this.content = content;
            this.messageType = messageType;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class LatestChatMessageInfoDTO {

        private String content;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime sentAt;

        @Builder
        public LatestChatMessageInfoDTO(String content, LocalDateTime sentAt) {
            this.content = content;
            this.sentAt = sentAt;
        }
    }
}
