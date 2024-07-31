package sumcoda.boardbuddy.enumerate;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParticipationApplicationStatus {

    NONE("none"), // 참여 없음 상태
    PENDING("pending"),  // 참여 대기 상태
    APPROVED("approved"), // 참여 승인 상태
    REJECTED("rejected"), // 참여 거절 상태
    CANCELED("canceled");  // 참여 취소 상태

    @JsonValue
    private final String value;
}
