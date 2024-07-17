package sumcoda.boardbuddy.repository;

import sumcoda.boardbuddy.dto.GatherArticleResponse;

import java.util.List;

public interface GatherArticleRepositoryCustom {
    List<GatherArticleResponse.GatherArticleDTO> findGatherArticleDTOByUsername(String username);
}
