package sumcoda.boardbuddy.repository;

import sumcoda.boardbuddy.dto.ChatMessageResponse;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepositoryCustom {

    List<ChatMessageResponse.ChatMessageInfoDTO> findMessagesAfterMemberJoinedByChatRoomIdAndUsername(Long chatRoomId, String username);

    Optional<ChatMessageResponse.ChatMessageInfoDTO> findTalkMessageById(Long chatMessageId);

    Optional<ChatMessageResponse.EnterOrExitMessageInfoDTO> findEnterOrExitMessageById(Long chatMessageId);

}
