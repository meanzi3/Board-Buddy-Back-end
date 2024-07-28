package sumcoda.boardbuddy.repository.memberGatherArticle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.MemberGatherArticle;

import java.util.Optional;

public interface MemberGatherArticleRepository extends JpaRepository<MemberGatherArticle, Long>, MemberGatherArticleRepositoryCustom {
    Boolean existsByGatherArticleIdAndMemberUsername(Long gatherArticleId, String username);

    Optional<MemberGatherArticle> findByGatherArticleIdAndMemberUsername(Long gatherArticleId, String username);

    Optional<MemberGatherArticle> findByParticipationApplicationId(Long participationApplicationId);

    Boolean existsByParticipationApplicationId(Long participationApplicationId);
}