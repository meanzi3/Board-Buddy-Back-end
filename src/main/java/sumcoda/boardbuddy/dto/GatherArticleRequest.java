package sumcoda.boardbuddy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import sumcoda.boardbuddy.entity.GatherArticle;

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

    private String location;

    private String sido;

    private String sigu;

    private String dong;

    private Integer maxParticipants;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Builder
    public CreateDTO(String title, String description, String location, String sido, String sigu, String dong, Integer maxParticipants, LocalDateTime startTime, LocalDateTime endTime) {
      this.title = title;
      this.description = description;
      this.location = location;
      this.sido = sido;
      this.sigu = sigu;
      this.dong = dong;
      this.maxParticipants = maxParticipants;
      this.startTime = startTime;
      this.endTime = endTime;
    }

    // 엔티티로 변환
    public GatherArticle toEntity() {
      return GatherArticle.createGatherArticle(this.title, this.maxParticipants, this.description, this.startTime, this.endTime,
              this.sido, this. sigu, this.dong,  this.location);
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

    private String location;

    private String sido;

    private String sigu;

    private String dong;

    private Integer maxParticipants;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Builder
    public UpdateDTO(String title, String description, String location, String sido, String sigu, String dong, Integer maxParticipants, LocalDateTime startTime, LocalDateTime endTime) {
      this.title = title;
      this.description = description;
      this.location = location;
      this.sido = sido;
      this.sigu = sigu;
      this.dong = dong;
      this.maxParticipants = maxParticipants;
      this.startTime = startTime;
      this.endTime = endTime;
    }

    // 엔티티 수정
    public void updateEntity(GatherArticle gatherArticle) {
      gatherArticle.update(this.title, this.description, this.location, this.sido, this.sigu, this.dong, this.maxParticipants, this.startTime, this.endTime);
    }

  }

}
