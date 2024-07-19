package sumcoda.boardbuddy.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {

    SUCCESS("success"),
    FAILURE("failure"),
    ERROR("error");

    private final String value;
    }
