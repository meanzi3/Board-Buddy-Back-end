package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.GatherArticleStatus;

import java.time.LocalDateTime;

public class GatherArticleResponse {

    @Getter
    @NoArgsConstructor
    public static class GatherArticleDTO {

        private Long id;

        private String title;

        private String description;

        private String meetingLocation;

        private Integer maxParticipants;

        private Integer currentParticipants;

        private LocalDateTime meetingDate;

        private LocalDateTime meetingEndDate;

        private LocalDateTime createdAt;

        private GatherArticleStatus status;

        @Builder
        public GatherArticleDTO (Long id, String title, String description, String meetingLocation, Integer maxParticipants, Integer currentParticipants, LocalDateTime meetingDate, LocalDateTime meetingEndDate, LocalDateTime createdAt, GatherArticleStatus status) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.meetingLocation = meetingLocation;
            this.maxParticipants = maxParticipants;
            this.currentParticipants = currentParticipants;
            this.meetingDate = meetingDate;
            this.meetingEndDate = meetingEndDate;
            this.createdAt = createdAt;
            this.status = status;
        }
    }
}
