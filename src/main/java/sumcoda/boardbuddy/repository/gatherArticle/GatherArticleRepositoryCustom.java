package sumcoda.boardbuddy.repository.gatherArticle;

import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.entity.Member;

import java.time.LocalDateTime;
import java.util.List;


public interface GatherArticleRepositoryCustom {
    List<GatherArticleResponse.GatherArticleInfosDTO> findGatherArticleInfosByUsername(String username);

    List<GatherArticleResponse.GatherArticleInfosDTO> findParticipationsByUsername(String username);

    long countGatherArticlesByMember(Member member, LocalDateTime startOfLastMonth, LocalDateTime endOfLastMonth);
}
