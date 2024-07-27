package sumcoda.boardbuddy.repository.memberGatherArticle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.MemberGatherArticle;

@Repository
public interface MemberGatherArticleRepository extends JpaRepository<MemberGatherArticle, Long>, MemberGatherArticleRepositoryCustom {
}
