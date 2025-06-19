package sumcoda.boardbuddy.repository.chatRoom;

import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.dto.ChatRoomResponse;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepositoryCustom {

    Optional<ChatRoomResponse.ValidateDTO> findValidateDTOByGatherArticleId(Long gatherArticleId);

    List<ChatRoomResponse.ChatRoomDetailsProjectionDTO> findChatRoomDetailsListByUsername(String username);
}
