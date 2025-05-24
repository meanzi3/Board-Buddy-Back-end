package sumcoda.boardbuddy.repository.chatMessage;

import sumcoda.boardbuddy.dto.ChatMessageResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepositoryCustom {

    List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> findInitialMessagesByChatRoomIdAndUsernameAndJoinedAt(Long chatRoomId, String username, LocalDateTime joinedAt);

    List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> findNewerMessagesByChatRoomIdAndUsernameAndJoinedAtAndCursor(Long chatRoomId, String username, LocalDateTime joinedAt, LocalDateTime cursorSentAt, Long cursorId);

    List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> findOlderMessagesByChatRoomIdAndUsernameAndJoinedAtAndCursor(Long chatRoomId, String username, LocalDateTime joinedAt, LocalDateTime cursorSentAt, Long cursorId);

    LocalDateTime findJoinedAtByChatRoomIdAndUsername(Long chatRoomId, String username);

    Optional<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> findTalkMessageById(Long chatMessageId);

    Optional<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> findEnterOrExitMessageById(Long chatMessageId);

}
