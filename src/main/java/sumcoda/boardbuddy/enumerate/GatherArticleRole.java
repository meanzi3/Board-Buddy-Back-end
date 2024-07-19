package sumcoda.boardbuddy.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GatherArticleRole {

    AUTHOR("author"),
    PARTICIPANT("participant");

    private final String value;


}
