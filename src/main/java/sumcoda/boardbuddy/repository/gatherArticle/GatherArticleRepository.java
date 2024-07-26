package sumcoda.boardbuddy.repository.gatherArticle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.GatherArticle;

@Repository
public interface GatherArticleRepository extends JpaRepository<GatherArticle, Long>, GatherArticleRepositoryCustom {
}
