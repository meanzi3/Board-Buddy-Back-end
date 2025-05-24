package sumcoda.boardbuddy.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.util.Pair;
import sumcoda.boardbuddy.dto.ChatMessageResponse;
import sumcoda.boardbuddy.dto.common.PageResponse;
import sumcoda.boardbuddy.enumerate.MessageType;
import sumcoda.boardbuddy.exception.ChatMessageRetrievalException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChatMessageUtil {

    public static final int CHAT_MESSAGE_PAGE_SIZE = 50;

    private static final DateTimeFormatter CURSOR_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * 주어진 메시지 프로젝션 DTO를 채팅 메시지 항목 DTO로 변환
     *
     * @param responseChatMessage 변환할 메시지 프로젝션 DTO
     * @return 변환된 ChatMessageItemInfoDTO 객체
     * @since 1.0
     * @version 2.0
     */
    public static ChatMessageResponse.ChatMessageItemInfoDTO convertPayload(
            ChatMessageResponse.ChatMessageItemInfoProjectionDTO responseChatMessage) {
        // 1) 공통 필드 세팅
        var builder = ChatMessageResponse.ChatMessageItemInfoDTO.builder()
                .id(responseChatMessage.getId())
                .content(responseChatMessage.getContent())
                .messageType(responseChatMessage.getMessageType());

        // 2) Talk 타입일 때만 추가 필드 세팅
        // 성능 개선용
        if (responseChatMessage.getMessageType().equals("TALK")) {
            builder.nickname(responseChatMessage.getNickname())
                    .profileImageURL(responseChatMessage.getProfileImageURL())
                    .rank(responseChatMessage.getRank())
                    .sentAt(responseChatMessage.getSentAt());
        }

//        // 2) Talk 타입일 때만 추가 필드 세팅
//        if (Objects.equals(responseChatMessage.getMessageType(), MessageType.TALK) {
//            builder
//                    .nickname(responseChatMessage.getNickname())
//                    .profileImageURL(responseChatMessage.getProfileImageURL())
//                    .rank(responseChatMessage.getRank())
//                    .sentAt(responseChatMessage.getSentAt());
//        }

        // 3) 빌드해서 반환
        return builder.build();
    }

    /**
     * 페이징 조회 결과를 클라이언트 제공용 DTO로 변환
     *
     * @param chatMessages 레포지토리에서 내려온 PageResponse
     * @param hasMore 조회 방향으로 메시지가 더 있는지 여부
     * @param nextCursor 조회 방향에 따른 첫번째 또는 마지막 메시지를 가리키는 커서
     * @return 클라이언트 제공용 ChatMessageItemInfoDTO 페이징 결과
     * @throws ChatMessageRetrievalException 데이터가 없거나 매핑에 실패할 때
     * @since 1.0
     * @version 2.0
     */
    public static PageResponse<ChatMessageResponse.ChatMessageItemInfoDTO> convertToChatMessagePageResponse(
            List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> chatMessages,
            Boolean hasMore,
            String nextCursor) {

        // 빈 페이지 예외 처리
        if (chatMessages.isEmpty()) {
            throw new ChatMessageRetrievalException(
                    "서버 문제로 메시지를 조회하지 못하였습니다. 관리자에게 문의하세요"
            );
        }

        // 내부 DTO → 최종 API DTO 매핑
        List<ChatMessageResponse.ChatMessageItemInfoDTO> chatMessageItemInfoDTOList = chatMessages.stream()
                .map(chatMessage -> {
                    var builder = ChatMessageResponse.ChatMessageItemInfoDTO.builder()
                                    .id(chatMessage.getId())
                                    .content(chatMessage.getContent())
                                    .messageType(chatMessage.getMessageType());

//                    // TALK 타입에만 추가 필드 설정
//                    if (Objects.equals(message.getMessageType(), MessageType.TALK)) {
//                        builder.nickname(message.getNickname())
//                                .profileImageS3SavedURL(message.getProfileImageS3SavedURL())
//                                .rank(message.getRank())
//                                .sentAt(message.getSentAt());
//                    }

                    // TALK 타입에만 추가 필드 설정
                    if (Objects.equals(chatMessage.getMessageType(), "TALK")) {
                        builder.nickname(chatMessage.getNickname())
                                .profileImageURL(chatMessage.getProfileImageURL())
                                .rank(chatMessage.getRank())
                                .sentAt(chatMessage.getSentAt());
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());

        // 기존 page 의 hasMore, nextCursor 를 그대로 담아 새 PageResponse 생성
        return PageResponse.<ChatMessageResponse.ChatMessageItemInfoDTO>builder()
                .dataList(chatMessageItemInfoDTOList)
                .hasMore(hasMore)
                .nextCursor(nextCursor)
                .build();
    }

    /**
     * 페이지 크기에 따라 메시지 프로젝션 리스트에서 일부 항목을 반환
     *
     * @param chatMessagesItemList 전체 메시지 프로젝션 리스트
     * @param hasMore 추가로 가져올 메시지가 있는지 여부
     * @return CHAT_MESSAGE_PAGE_SIZE 만큼 잘라낸 리스트 또는 전체 리스트
     * @since 1.0
     * @version 2.0
     */
    public static List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> getSubChatMessageItemInfoProjectionDTOList(
            List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> chatMessagesItemList,
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
     * 메시지 프로젝션 DTO로부터 페이지네이션 커서 문자열을 생성
     *
     * @param message 커서를 생성할 메시지 프로젝션 DTO
     * @return ISO_LOCAL_DATE_TIME 포맷의 sentAt과 ID를 언더스코어로 결합한 커서 문자열
     * @since 1.0
     * @version 2.0
     */
    public static String formatCursor(ChatMessageResponse.ChatMessageItemInfoProjectionDTO message) {
        // sentAt을 ISO_LOCAL_DATE_TIME 포맷으로 직렬화하고, ID와 언더스코어로 결합
        return message.getSentAt().format(CURSOR_FORMATTER)
                + "_"
                + message.getId();
    }

    /**
     * opaque 커서 문자열을 분해해 sentAt과 id 추출
     *
     * @param cursor ISO_LOCAL_DATE_TIME 형식의 날짜와 '_' 구분자 결합된 메시지 ID
     * @return 분해된 전송 시각(LocalDateTime)과 메시지 ID(Long) Pair
     * @since 1.0
     * @version 2.0
     */
    public static Pair<LocalDateTime, Long> parseCursor(String cursor) {
        String[] parts = cursor.split("_");

        LocalDateTime cursorSentAt = LocalDateTime.parse(parts[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Long cursorId = Long.valueOf(parts[1]);

        return Pair.of(cursorSentAt, cursorId);
    }

    /**
     * 주어진 방향에 따라 다음 페이지 조회용 커서를 계산
     *
     * @param dataList  ASC(오래된→최신) 순으로 정렬된 메시지 프로젝션 리스트
     * @param hasMore   추가로 가져올 메시지가 있는지 여부
     * @param isNewer   true면 최신 방향 페이지네이션, false면 과거 방향 페이지네이션
     * @return 다음 조회 커서 문자열 또는 더 이상 없으면 null
     * @since 1.0
     * @version 2.0
     */
    @Nullable
    public static String getNextCursor(
            List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> dataList,
            Boolean hasMore,
            boolean isNewer
    ) {
        // 더 가져올 메시지가 없거나, 데이터 자체가 비어 있으면 null 반환
        if (!hasMore || dataList.isEmpty()) {
            return null;
        }

        int idx = isNewer
                ? dataList.size() - 1 // 최신 방향: 마지막 인덱스
                : 0; // 과거 방향: 첫 인덱스

        return formatCursor(dataList.get(idx));
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
