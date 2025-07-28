package sumcoda.boardbuddy.repository.participationApplication;

import sumcoda.boardbuddy.dto.ParticipationApplicationResponse;
import sumcoda.boardbuddy.dto.fetch.ParticipationApplicationInfoProjection;
import sumcoda.boardbuddy.entity.ParticipationApplication;

import java.util.List;
import java.util.Optional;

public interface ParticipationApplicationRepositoryCustom {

    Boolean existsByGatherArticleIdAndUsername(Long gatherArticleId, String username);

    Optional<ParticipationApplication> findByGatherArticleIdAndUsername(Long gatherArticleId, String username);

    List<ParticipationApplicationInfoProjection> findParticipationAppliedMemberByGatherArticleId(Long gatherArticleId);
}
