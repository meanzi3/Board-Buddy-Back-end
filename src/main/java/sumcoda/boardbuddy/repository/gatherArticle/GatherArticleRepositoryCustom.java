package sumcoda.boardbuddy.repository.gatherArticle;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GatherArticleRepositoryCustom {
    List<GatherArticleResponse.GatherArticleInfosDTO> findGatherArticleInfosByUsername(String username);

    List<GatherArticleResponse.GatherArticleInfosDTO> findParticipationsByUsername(String username);

    Boolean isMemberAuthorOfGatherArticle(Long gatherArticleId, String username);
  
    long countGatherArticlesByMember(Member member, LocalDateTime startOfLastMonth, LocalDateTime endOfLastMonth);

    Slice<GatherArticleResponse.ReadSliceDTO> findReadSliceDTOByLocationAndStatusAndSort(
            List<String> sidoList, List<String> siguList, List<String> dongList, String status, String sort, Pageable pageable);
}