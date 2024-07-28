package sumcoda.boardbuddy.repository.gatherArticle;

import sumcoda.boardbuddy.dto.GatherArticleResponse;

import java.util.List;
import java.util.Optional;


public interface GatherArticleRepositoryCustom {
    List<GatherArticleResponse.GatherArticleInfosDTO> findGatherArticleInfosByUsername(String username);

    List<GatherArticleResponse.GatherArticleInfosDTO> findParticipationsByUsername(String username);

    Boolean isMemberAuthorOfGatherArticle(Long gatherArticleId, String username);
}
