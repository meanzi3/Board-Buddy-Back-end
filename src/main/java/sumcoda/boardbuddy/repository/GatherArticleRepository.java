package sumcoda.boardbuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.entity.GatherArticle;

import java.util.List;

@Repository
public interface GatherArticleRepository extends JpaRepository<GatherArticle, Long>, GatherArticleRepositoryCustom {
    List<GatherArticleResponse.GatherArticleDTO> findGatherArticleDTOByUsername(String username);
}
