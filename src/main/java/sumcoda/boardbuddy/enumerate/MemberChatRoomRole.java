package sumcoda.boardbuddy.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberChatRoomRole {

    HOST("host"),
    PARTICIPANT("participant");

    private final String value;
}
