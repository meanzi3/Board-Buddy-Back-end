package sumcoda.boardbuddy.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatMessageRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PublishDTO {
        private String content;

        private String nickname;

        @Builder
        public PublishDTO(String content, String nickname) {
            this.content = content;
            this.nickname = nickname;
        }
    }
}
