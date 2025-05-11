package sumcoda.boardbuddy.repository.gatherArticle;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.enumerate.MemberGatherArticleRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GatherArticleRepositoryCustom {
    List<GatherArticleResponse.GatherArticleInfosDTO> findGatherArticleInfosByUsername(String username);

    List<GatherArticleResponse.MyParticipationInfosDTO> findParticipationsByUsername(String username);

    Boolean isMemberAuthorOfGatherArticle(Long gatherArticleId, String username);

    long countGatherArticlesByMember(Member member, LocalDateTime startOfLastMonth, LocalDateTime endOfLastMonth);

    Optional<GatherArticleResponse.IdDTO> findIdDTOById(Long gatherArticleId);

    Slice<GatherArticleResponse.ReadSliceDTO> findReadSliceDTOByLocationAndStatusAndSort(
            List<String> sidoList, List<String> sggList, List<String> emdList,
            String status, String sort, MemberGatherArticleRole role, Pageable pageable);

    Optional<GatherArticleResponse.SummaryInfoDTO> findSimpleInfoByGatherArticleId(Long gatherArticleId);

    GatherArticleResponse.ReadDTO findGatherArticleReadDTOByGatherArticleId(Long gatherArticleId, Long memberId);

    Optional<GatherArticleResponse.TitleDTO> findTitleDTOById(Long gatherArticleId);

    Optional<GatherArticleResponse.LocationInfoDTO> findLocationInfoDTOById(Long gatherArticleId);

    List<GatherArticleResponse.SearchResultDTO> findSearchResultDTOByKeyword(
            List<String> sidoList, List<String> sggList, List<String> emdList, MemberGatherArticleRole role, String keyword);

    Optional<GatherArticleResponse.StatusDTO> findStatusDTOById(Long gatherArticleId);

    List<Long> findGatherArticleIdsByUsername(String username);

}