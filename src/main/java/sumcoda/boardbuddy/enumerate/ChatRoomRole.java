package sumcoda.boardbuddy.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatRoomRole {

    AUTHOR("AUTHOR"),
    PARTICIPANT("PARTICIPANT");

    private final String value;
}
