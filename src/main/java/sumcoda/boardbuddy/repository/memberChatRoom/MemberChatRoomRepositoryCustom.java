package sumcoda.boardbuddy.repository.memberChatRoom;

import sumcoda.boardbuddy.dto.MemberChatRoomResponse;

import java.util.Optional;

public interface MemberChatRoomRepositoryCustom {

    Boolean existsByGatherArticleIdAndNickname(Long gatherArticleId, String nickname);

    Optional<MemberChatRoomResponse.ValidateDTO> findByGatherArticleIdAndUsername(Long gatherArticleId, String username);
}
