package sumcoda.boardbuddy.repository.chatMessage;

import sumcoda.boardbuddy.dto.fetch.ChatMessageItemInfoProjection;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepositoryCustom {

    List<ChatMessageItemInfoProjection> findInitialMessagesByChatRoomIdAndUsernameAndJoinedAt(Long chatRoomId, String username, Instant joinedAt);

    List<ChatMessageItemInfoProjection> findNewerMessagesByChatRoomIdAndUsernameAndJoinedAtAndCursor(Long chatRoomId, String username, Instant joinedAt, Instant cursorSentAt, Long cursorId);

    List<ChatMessageItemInfoProjection> findOlderMessagesByChatRoomIdAndUsernameAndJoinedAtAndCursor(Long chatRoomId, String username, Instant joinedAt, Instant cursorSentAt, Long cursorId);

    LocalDateTime findJoinedAtByChatRoomIdAndUsername(Long chatRoomId, String username);

    Optional<ChatMessageItemInfoProjection> findChatMessageById(Long chatMessageId);

}
