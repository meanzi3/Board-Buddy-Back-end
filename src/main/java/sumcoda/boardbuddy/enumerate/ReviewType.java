package sumcoda.boardbuddy.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewType {

    EXCELLENT("excellent", 0.25),
    GOOD("good", 0.15),
    BAD("bad", -0.05),
    NOSHOW("noshow", -0.15);

    private final String value;
    private final double score;
}
