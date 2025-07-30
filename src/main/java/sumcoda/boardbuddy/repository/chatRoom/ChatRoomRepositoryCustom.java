package sumcoda.boardbuddy.repository.chatRoom;

import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.dto.ChatRoomResponse;
import sumcoda.boardbuddy.dto.fetch.ChatRoomInfoProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepositoryCustom {

    Optional<ChatRoomResponse.ValidateDTO> findValidateDTOByGatherArticleId(Long gatherArticleId);

    List<ChatRoomInfoProjection> findChatRoomInfoProjectionsByUsername(String username);
}
