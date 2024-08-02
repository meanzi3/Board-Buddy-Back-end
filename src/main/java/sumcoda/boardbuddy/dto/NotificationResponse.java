package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationResponse {

    @Getter
    @NoArgsConstructor
    public static class NotificationDTO {

        // 알림 내용
        String content;

        @Builder
        public NotificationDTO(String content) {
            this.content = content;
        }
    }
}
