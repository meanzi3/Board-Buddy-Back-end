package sumcoda.boardbuddy.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RadiusRange {

    FAR_DONG(10),
    LITTLE_FAR_DONG(7),
    LITTLE_CLOSE_DONG(5),
    CLOSE_DONG(2);

    private final int radius;
}
