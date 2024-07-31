package sumcoda.boardbuddy.util;

import sumcoda.boardbuddy.enumerate.MessageType;

public class ChatMessageUtil {

    /**
     * 채팅방 입장/퇴장 메시지 내용을 생성
     *
     * @param nickname 사용자 닉네임
     * @param messageType 메시지 유형 (입장/퇴장)
     * @return 생성된 채팅 메시지 내용
     **/
    public static String buildChatMessageContent(String nickname, MessageType messageType) {
        String content = "";

        if (messageType.equals(MessageType.ENTER)) {
            content = "[입장] " + nickname + "님이 채팅방에 입장했습니다";
        } else if (messageType.equals(MessageType.EXIT)) {
            content = "[퇴장] " + nickname + "님이 채팅방에서 퇴장했습니다.";
        }

        return content;
    }
}
