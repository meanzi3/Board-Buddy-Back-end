package sumcoda.boardbuddy.util;

import sumcoda.boardbuddy.dto.GatherArticleRequest;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleSaveException;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleUpdateException;

import java.time.LocalDateTime;

public class GatherArticleValidationUtil {

    private GatherArticleValidationUtil() {}

    public static void validateCreateRequest(GatherArticleRequest.CreateDTO createRequest) {
        validateCommonFields(createRequest.getTitle(), createRequest.getDescription(),
                createRequest.getMeetingLocation(), createRequest.getSido(),
                createRequest.getSgg(), createRequest.getEmd(),
                createRequest.getX(), createRequest.getY(),
                createRequest.getStartDateTime(), createRequest.getEndDateTime(),
                createRequest.getMaxParticipants());
    }

    public static void validateUpdateRequest(GatherArticleRequest.UpdateDTO updateRequest, int currentParticipants) {
        validateCommonFields(updateRequest.getTitle(), updateRequest.getDescription(),
                updateRequest.getMeetingLocation(), updateRequest.getSido(),
                updateRequest.getSgg(), updateRequest.getEmd(),
                updateRequest.getX(), updateRequest.getY(),
                updateRequest.getStartDateTime(), updateRequest.getEndDateTime(),
                updateRequest.getMaxParticipants());

        if (updateRequest.getMaxParticipants() < currentParticipants) {
            throw new GatherArticleUpdateException("최대 참가 인원은 현재 참가 인원보다 적을 수 없습니다.\n 현재 참가 인원 : "
                    + currentParticipants + "명");
        }
    }

    private static void validateCommonFields(String title, String description, String meetingLocation,
                                             String sido, String sgg, String emd,
                                             Double x, Double y, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer maxParticipants) {
        if (title == null || title.isEmpty()) {
            throw new GatherArticleSaveException("제목이 입력되지 않았습니다.");
        }
        if (description == null || description.isEmpty()) {
            throw new GatherArticleSaveException("설명이 입력되지 않았습니다.");
        }
        if (meetingLocation == null || meetingLocation.isEmpty()) {
           throw new GatherArticleSaveException("장소가 입력되지 않았습니다.");
        }
        if (sido == null || sido.isEmpty()) {
            throw new GatherArticleSaveException("시, 도가 입력되지 않았습니다.");
        }
        if (sgg == null || sgg.isEmpty()) {
            throw new GatherArticleSaveException("시, 군, 구가 입력되지 않았습니다.");
        }
        if (emd == null || emd.isEmpty()) {
           throw new GatherArticleSaveException("읍, 면, 동이 입력되지 않았습니다.");
        }
        if (x == null) {
           throw new GatherArticleSaveException("경도가 입력되지 않았습니다.");
        }
        if (y == null) {
           throw new GatherArticleSaveException("위도가 입력되지 않았습니다.");
        }
        if (startDateTime == null) {
            throw new GatherArticleSaveException("시작 시간이 입력되지 않았습니다.");
        }
        if (endDateTime == null) {
           throw new GatherArticleSaveException("종료 시간이 입력되지 않았습니다.");
        }
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new GatherArticleSaveException("시작 시간은 현재 시간보다 늦어야 합니다.");
        }
        if (endDateTime.isBefore(LocalDateTime.now())) {
            throw new GatherArticleSaveException("종료 시간은 현재 시간보다 늦어야 합니다.");
        }
        if (endDateTime.isBefore(startDateTime)) {
            throw new GatherArticleSaveException("종료 시간은 시작 시간보다 늦어야 합니다.");
        }
        if (maxParticipants == null || maxParticipants <= 0) {
            throw new GatherArticleSaveException("최대 참가 인원이 유효하지 않습니다.");
        }
    }
}
