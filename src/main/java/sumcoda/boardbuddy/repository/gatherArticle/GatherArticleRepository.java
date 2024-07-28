package sumcoda.boardbuddy.repository.gatherArticle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.GatherArticle;

import java.util.Optional;

@Repository
public interface GatherArticleRepository extends JpaRepository<GatherArticle, Long>, GatherArticleRepositoryCustom {

    Optional<GatherArticle> findById(Long gatherArticleId);

    boolean existsById(Long gatherArticleId);
}