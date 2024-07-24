package sumcoda.boardbuddy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import sumcoda.boardbuddy.entity.GatherArticle;
import sumcoda.boardbuddy.enumerate.GatherArticleStatus;

import java.time.LocalDateTime;

public class GatherArticleRequest {

  /**
   * 작성 요청 dto
   */
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class CreateDTO {
    private String title;

    private String description;

    private String meetingLocation;

    private String sido;

    private String sigu;

    private String dong;

    private Double x;

    private Double y;

    private Integer maxParticipants;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;

    @Builder
    public CreateDTO(String title, String description, String meetingLocation, String sido, String sigu, String dong, Double x, Double y, Integer maxParticipants, LocalDateTime startDateTime, LocalDateTime endDateTime) {
      this.title = title;
      this.description = description;
      this.meetingLocation = meetingLocation;
      this.sido = sido;
      this.sigu = sigu;
      this.dong = dong;
      this.x = x;
      this.y = y;
      this.maxParticipants = maxParticipants;
      this.startDateTime = startDateTime;
      this.endDateTime = endDateTime;
    }

    // 엔티티로 변환
    public GatherArticle toEntity() {
      return GatherArticle.buildGatherArticle(this.title, 1, this.maxParticipants, GatherArticleStatus.OPEN, this.description, this.startDateTime, this.endDateTime,
              this.sido, this. sigu, this.dong,  this.meetingLocation, this.x, this.y);
    }
  }

  /**
   * 수정 요청 dto
   */
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UpdateDTO {

    private String title;

    private String description;

    private String meetingLocation;

    private String sido;

    private String sigu;

    private String dong;

    private Double x;

    private Double y;

    private Integer maxParticipants;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;

    @Builder
    public UpdateDTO(String title, String description, String meetingLocation, String sido, String sigu, String dong, Double x, Double y, Integer maxParticipants, LocalDateTime startDateTime, LocalDateTime endDateTime) {
      this.title = title;
      this.description = description;
      this.meetingLocation = meetingLocation;
      this.sido = sido;
      this.sigu = sigu;
      this.dong = dong;
      this.x = x;
      this.y = y;
      this.maxParticipants = maxParticipants;
      this.startDateTime = startDateTime;
      this.endDateTime = endDateTime;
    }

    // 엔티티 수정
    public void updateEntity(GatherArticle gatherArticle) {
      gatherArticle.update(this.title, this.description, this.meetingLocation, this.sido, this.sigu, this.dong, this.x, this. y, this.maxParticipants, this.startDateTime, this.endDateTime);
    }

  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadListDTO {
    private Integer page;
    private String status;
    private String sort;

    @Builder
    public ReadListDTO(Integer page, String status, String sort) {
      this.page = page;
      this.status = status;
      this.sort = sort;
    }
  }
}
