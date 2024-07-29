package sumcoda.boardbuddy.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateDTO {
        private String content;

        @Builder
        public CreateDTO(String content) {
            this.content = content;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateDTO {
        private String content;

        @Builder
        public UpdateDTO(String content) {
            this.content = content;
        }
    }
}
