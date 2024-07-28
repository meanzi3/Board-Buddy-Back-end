package sumcoda.boardbuddy.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberGatherArticleRole {

    NONE("none"),
    AUTHOR("author"),
    PARTICIPANT("participant");

    private final String value;


}
