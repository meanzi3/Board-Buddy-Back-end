package sumcoda.boardbuddy.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageType {

    ENTER("enter"),
    EXIT("exit"),
    TALK("talk");

    private final String value;
}
