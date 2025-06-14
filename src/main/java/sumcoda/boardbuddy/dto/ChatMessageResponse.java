package sumcoda.boardbuddy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.MessageType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class ChatMessageResponse {

//    @Getter
//    @NoArgsConstructor
//    public static class ChatMessageItemInfoDTO {
//
//        private String content;
//
//        private String nickname;
//
//        private String profileImageS3SavedURL;
//
//        private Integer rank;
//
//        // private String messageType;
//
//        private MessageType messageType;
//
//        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
//        private LocalDateTime sentAt;
//
//        @Builder
//        public ChatMessageItemInfoDTO(String content, String nickname, String profileImageS3SavedURL, Integer rank, MessageType messageType, LocalDateTime sentAt) {
//            this.content = content;
//            this.nickname = nickname;
//            this.profileImageS3SavedURL = profileImageS3SavedURL;
//            this.rank = rank;
//            this.messageType = messageType;
//            this.sentAt = sentAt;
//        }
//    }

    // 성능 개선용(V2)
    @Getter
    @NoArgsConstructor
    public static class ChatMessageItemInfoProjectionDTO {

        private Long id;

        private String content;

        private String nickname;

        private String profileImageS3SavedURL;

        private Integer rank;

        private MessageType messageType;

        // 성능 개선용
//        private String messageType;

        private Instant sentAt;

        @Builder
        public ChatMessageItemInfoProjectionDTO(Long id, String content, String nickname, String profileImageS3SavedURL, Integer rank, MessageType messageType, Instant sentAt) {
            this.id = id;
            this.content = content;
            this.nickname = nickname;
            this.profileImageS3SavedURL = profileImageS3SavedURL;
            this.rank = rank;
            this.messageType = messageType;
            this.sentAt = sentAt;
        }
    }

    // 성능 개선용(V2)
    @Getter
    @NoArgsConstructor
    public static class ChatMessageItemInfoDTO {

        private Long id;

        private String content;

        private String nickname;

        private String profileImageS3SavedURL;

        private Integer rank;

        private MessageType messageType;

//        // 성능 개선용
//        private String messageType;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        private LocalDateTime sentAt;

        @Builder
        public ChatMessageItemInfoDTO(Long id, String content, String nickname, String profileImageS3SavedURL, Integer rank, MessageType messageType, LocalDateTime sentAt) {
            this.id = id;
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
    public static class ChatMessagePageInfoDTO {

        private List<ChatMessageItemInfoDTO> dataList;

        private Boolean hasMore;

        private String nextCursor;

        @Builder
        public ChatMessagePageInfoDTO(List<ChatMessageItemInfoDTO> dataList, Boolean hasMore, String nextCursor) {
            this.dataList = dataList;
            this.hasMore = hasMore;
            this.nextCursor = nextCursor;
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