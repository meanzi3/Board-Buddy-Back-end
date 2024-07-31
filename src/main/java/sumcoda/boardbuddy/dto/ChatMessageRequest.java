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

        @Builder
        public PublishDTO(String content) {
            this.content = content;
        }
    }
}
