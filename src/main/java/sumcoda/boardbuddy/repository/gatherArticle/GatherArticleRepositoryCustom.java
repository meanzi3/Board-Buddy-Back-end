package sumcoda.boardbuddy.repository.gatherArticle;

import sumcoda.boardbuddy.dto.GatherArticleResponse;

import java.util.List;


public interface GatherArticleRepositoryCustom {
    List<GatherArticleResponse.GatherArticleInfosDTO> findGatherArticleInfosByUsername(String username);

    List<GatherArticleResponse.GatherArticleInfosDTO> findParticipationsByUsername(String username);
}
