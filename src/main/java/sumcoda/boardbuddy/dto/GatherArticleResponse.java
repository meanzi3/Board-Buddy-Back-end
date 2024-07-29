package sumcoda.boardbuddy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.GatherArticleStatus;
import sumcoda.boardbuddy.enumerate.ParticipationApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;

public class GatherArticleResponse {

    @Getter
    @NoArgsConstructor
    public static class GatherArticleInfosDTO {

        private Long id;

        private String title;

        private String description;

        private String meetingLocation;

        private Integer maxParticipants;

        private Integer currentParticipants;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startDateTime;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endDateTime;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdAt;

        private GatherArticleStatus status;

        @Builder
        public GatherArticleInfosDTO(Long id, String title, String description, String meetingLocation, Integer maxParticipants, Integer currentParticipants, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime createdAt, GatherArticleStatus status) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.meetingLocation = meetingLocation;
            this.maxParticipants = maxParticipants;
            this.currentParticipants = currentParticipants;
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
            this.createdAt = createdAt;
            this.status = status;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CreateDTO {
        private Long id;

        @Builder
        public CreateDTO(Long id) {
            this.id = id;
        }

    }

    @Getter
    @NoArgsConstructor
    public static class ReadDTO {
        private String title;
        private String description;
        private GatherArticleResponse.AuthorDTO author;
        private String sido;
        private String sgg;
        private String emd;
        private String meetingLocation;
        private Double x;
        private Double y;
        private Integer maxParticipants;
        private Integer currentParticipants;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startDateTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endDateTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdAt;
        private GatherArticleStatus status;
        private ParticipationApplicationStatus participationApplicationStatus;

        @Builder
        public ReadDTO(String title, String description, AuthorDTO author, String sido, String sgg, String emd, String meetingLocation, Double x, Double y,
                       Integer maxParticipants, Integer currentParticipants, LocalDateTime startDateTime,
                       LocalDateTime endDateTime, LocalDateTime createdAt, GatherArticleStatus status, ParticipationApplicationStatus participationApplicationStatus) {
            this.title = title;
            this.description = description;
            this.author = author;
            this.sido = sido;
            this.sgg = sgg;
            this.emd = emd;
            this.meetingLocation = meetingLocation;
            this.x = x;
            this.y = y;
            this.maxParticipants = maxParticipants;
            this.currentParticipants = currentParticipants;
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
            this.createdAt = createdAt;
            this.status = status;
            this.participationApplicationStatus = participationApplicationStatus;
        }

    }

    @Getter
    @NoArgsConstructor
    public static class AuthorDTO {
        private String nickname;
        private Integer rank;
        private String profileImageS3SavedURL;
        private String description;

        @Builder
        public AuthorDTO(String nickname, Integer rank, String profileImageS3SavedURL, String description) {
            this.nickname = nickname;
            this.rank = rank;
            this.profileImageS3SavedURL = profileImageS3SavedURL;
            this.description = description;
        }

    }

    @Getter
    @NoArgsConstructor
    public static class UpdateDTO {
        private Long id;

        @Builder
        public UpdateDTO(Long id) {
            this.id = id;
        }

    }

    @Getter
    @NoArgsConstructor
    public static class DeleteDTO {
        private Long id;

        @Builder
        public DeleteDTO(Long id) {
            this.id = id;
        }

    }

    @Getter
    @NoArgsConstructor
    public static class IdDTO {
        private Long id;

        @Builder
        public IdDTO(Long id) {
            this.id = id;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReadSliceDTO {
        private Long id;
        private String title;
        private String description;
        private AuthorSimpleDTO author;
        private String meetingLocation;
        private Integer maxParticipants;
        private Integer currentParticipants;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startDateTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endDateTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime createdAt;
        private GatherArticleStatus status;

        @Builder
        public ReadSliceDTO(Long id, String title, String description, AuthorSimpleDTO author, String meetingLocation,
                            Integer maxParticipants, Integer currentParticipants, LocalDateTime startDateTime,
                            LocalDateTime endDateTime, LocalDateTime createdAt, GatherArticleStatus status) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.author = author;
            this.meetingLocation = meetingLocation;
            this.maxParticipants = maxParticipants;
            this.currentParticipants = currentParticipants;
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
            this.createdAt = createdAt;
            this.status = status;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class AuthorSimpleDTO {
        private String nickname;
        private Integer rank;

        @Builder
        public AuthorSimpleDTO(String nickname, Integer rank) {
            this.nickname = nickname;
            this.rank = rank;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReadListDTO {

        private List<ReadSliceDTO> posts;
        private Boolean last;

        @Builder
        public ReadListDTO(List<ReadSliceDTO> posts, Boolean last) {
            this.posts = posts;
            this.last = last;
        }
    }
}
