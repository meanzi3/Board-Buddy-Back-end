package sumcoda.boardbuddy.dto.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import sumcoda.boardbuddy.enumerate.GatherArticleStatus;

import java.time.LocalDateTime;

@Builder
public record GatherArticleDetailedInfoDTO(

        String title,

        String description,

        GatherArticleAuthorDTO author,

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

        GatherArticleStatus status
) {}
