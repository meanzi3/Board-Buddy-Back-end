package sumcoda.boardbuddy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NotificationResponse {

    @Getter
    @NoArgsConstructor
    public static class NotificationDTO {

        // 알림 메세지
        private String message;

        // 알림 생성 시간
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdAt;

        @Builder
        public NotificationDTO(String message, LocalDateTime createdAt) {
            this.message = message;
            this.createdAt = createdAt;
        }
    }
}
