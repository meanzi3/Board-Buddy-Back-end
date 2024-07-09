package sumcoda.boardbuddy.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GatherArticleRole {

    AUTHOR("AUTHOR"),
    PARTICIPANT("PARTICIPANT");

    private final String value;
}
