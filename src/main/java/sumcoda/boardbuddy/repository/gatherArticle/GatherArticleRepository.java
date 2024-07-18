package sumcoda.boardbuddy.repository.gatherArticle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.entity.GatherArticle;

import java.util.List;

@Repository
public interface GatherArticleRepository extends JpaRepository<GatherArticle, Long>, sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepositoryCustom {
    List<GatherArticleResponse.GatherArticleInfosDTO> findGatherArticleInfosByUsername(String username);

    List<GatherArticleResponse.GatherArticleInfosDTO> findParticipationsByUsername(String username);
}
