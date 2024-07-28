package sumcoda.boardbuddy.repository.participationApplication;

import sumcoda.boardbuddy.dto.ParticipationApplicationResponse;
import sumcoda.boardbuddy.entity.ParticipationApplication;

import java.util.List;
import java.util.Optional;

public interface ParticipationApplicationRepositoryCustom {

    Boolean existsByGatherArticleInAndUsername(Long gatherArticleId, String username);

    Optional<ParticipationApplication> findByGatherArticleIdAndMemberUsername(Long gatherArticleId, String username);

    List<ParticipationApplicationResponse.InfoDTO> findParticipationAppliedMemberByGatherArticleId(Long gatherArticleId);
}
