package sumcoda.boardbuddy.dto.fetch;

import com.fasterxml.jackson.annotation.JsonFormat;
import sumcoda.boardbuddy.enumerate.GatherArticleStatus;

import java.time.LocalDateTime;

public record GatherArticleDetailedInfoProjection(

        String title,

        String description,

        String sido,

        String sgg,

        String emd,

        String meetingLocation,

        Double x,

        Double y,

        Integer maxParticipants,

        Integer currentParticipants,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startDateTime,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endDateTime,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdAt,

        GatherArticleStatus gatherArticleStatus
) {}
