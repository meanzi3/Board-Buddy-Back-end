package sumcoda.boardbuddy.repository.memberChatRoom;

import sumcoda.boardbuddy.dto.MemberChatRoomResponse;

import java.util.Optional;

public interface MemberChatRoomRepositoryCustom {

    Boolean existsByGatherArticleIdAndUsername(Long gatherArticleId, String username);

    Optional<MemberChatRoomResponse.ValidateDTO> findByGatherArticleIdAndUsername(Long gatherArticleId, String username);
}
