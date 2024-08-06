package sumcoda.boardbuddy.enumerate;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberType {

    REGULAR("regular"),
    SOCIAL("social");

    @JsonValue
    private final String value;
}
