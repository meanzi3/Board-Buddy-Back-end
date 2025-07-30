package sumcoda.boardbuddy.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.util.Pair;
import sumcoda.boardbuddy.dto.fetch.ChatMessageItemInfoProjection;
import sumcoda.boardbuddy.enumerate.MessageType;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatMessageUtil {

    public static final int CHAT_MESSAGE_PAGE_SIZE = 50;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static final ZoneId ZONE = ZoneId.of("Asia/Seoul");


    /**
     * 페이지 크기에 따라 메시지 프로젝션 리스트에서 일부 항목을 반환
     *
     * @param chatMessagesItemList 전체 메시지 프로젝션 리스트
     * @param hasMore 추가로 가져올 메시지가 있는지 여부
     * @return CHAT_MESSAGE_PAGE_SIZE 만큼 잘라낸 리스트 또는 전체 리스트
     * @since 1.0
     * @version 2.0
     */
    public static List<ChatMessageItemInfoProjection> getSubChatMessageItemInfoProjections(
            List<ChatMessageItemInfoProjection> chatMessagesItemList,
            Boolean hasMore) {

        return hasMore ? chatMessagesItemList.subList(0, CHAT_MESSAGE_PAGE_SIZE) : chatMessagesItemList;
    }

    /**
     * 메시지 리스트 크기가 페이지 크기를 초과하는지 여부를 판단
     *
     * @param chatMessagesItemListSize 전체 메시지 프로젝션 리스트 크기
     * @return 리스트 크기가 PAGE_SIZE보다 크면 true, 그렇지 않으면 false
     * @since 1.0
     * @version 2.0
     */
    @NotNull
    public static Boolean getHasMore(int chatMessagesItemListSize) {
        return chatMessagesItemListSize > CHAT_MESSAGE_PAGE_SIZE;
    }

    /**
     * opaque 커서 문자열을 분해해 sentAt과 id 추출
     *
     * @param cursor ISO_LOCAL_DATE_TIME 형식의 날짜와 '_' 구분자 결합된 메시지 ID
     * @return 분해된 전송 시각(LocalDateTime)과 메시지 ID(Long) Pair
     * @since 1.0
     * @version 2.0
     */
    public static Pair<Instant, Long> parseCursor(String cursor) {
        String[] parts = cursor.split("_");

        // long → Instant
        Instant cursorSentAt = Instant.ofEpochMilli(Long.parseLong(parts[0]));

        Long cursorId = Long.valueOf(parts[1]);

        return Pair.of(cursorSentAt, cursorId);
    }

    /**
     * 주어진 방향에 따라 다음 페이지 조회용 커서를 계산
     *
     * @param dataList  ASC(오래된→최신) 순으로 정렬된 메시지 프로젝션 리스트
     * @param hasMore   추가로 가져올 메시지가 있는지 여부
     * @return 다음 조회 커서 문자열 또는 더 이상 없으면 null
     * @since 1.0
     * @version 2.0
     */
    @Nullable
    public static String getNextCursor(
            List<ChatMessageItemInfoProjection> dataList,
            Boolean hasMore
    ) {
        if (hasMore) {
            // hasMore == true 이므로 list.size() >= pageSize+1
            // 여기서만 커서를 계산
            ChatMessageItemInfoProjection message
                    = dataList.get(CHAT_MESSAGE_PAGE_SIZE - 1);
            Long epochMilli = message.sentAt().toEpochMilli();
            Long id = message.id();

            return epochMilli + "_" + id;
        }

        return null;
    }

    /**
     * 채팅방 입장/퇴장 메시지 내용을 생성
     *
     * @param nickname 사용자 닉네임
     * @param messageType 메시지 유형 (입장/퇴장)
     * @return 생성된 채팅 메시지 내용
     * @version 1.0
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
