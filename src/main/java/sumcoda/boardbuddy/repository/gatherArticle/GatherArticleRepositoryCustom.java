package sumcoda.boardbuddy.repository.gatherArticle;

import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GatherArticleRepositoryCustom {
    List<GatherArticleResponse.GatherArticleInfosDTO> findGatherArticleInfosByUsername(String username);

    List<GatherArticleResponse.MyParticipationInfosDTO> findParticipationsByUsername(String username);

    Boolean isMemberAuthorOfGatherArticle(Long gatherArticleId, String username);

    long countGatherArticlesByMember(Member member, LocalDateTime startOfLastMonth, LocalDateTime endOfLastMonth);

    Optional<GatherArticleResponse.IdDTO> findIdDTOById(Long gatherArticleId);

    /**
     * @apiNote 임시 비활성화된 상태
     *          위치 관련 코드 제거 필요
     */
//    Slice<GatherArticleResponse.ReadSliceDTO> findReadSliceDTOByLocationAndStatusAndSort(
//            List<String> sidoList, List<String> sggList, List<String> emdList,
//            String status, String sort, MemberGatherArticleRole role, Pageable pageable);

    Optional<GatherArticleResponse.SummaryInfoDTO> findSimpleInfoByGatherArticleId(Long gatherArticleId);

    GatherArticleResponse.ReadDTO findGatherArticleReadDTOByGatherArticleId(Long gatherArticleId, Long memberId);

    Optional<GatherArticleResponse.TitleDTO> findTitleDTOById(Long gatherArticleId);

    /**
     * @apiNote 임시 비활성화된 상태
     *          위치 관련 코드 제거 필요
     */
//    Optional<GatherArticleResponse.LocationInfoDTO> findLocationInfoDTOById(Long gatherArticleId);

    /**
     * @apiNote 임시 비활성화된 상태
     *          위치 관련 코드 제거 필요
     */
//    List<GatherArticleResponse.SearchResultDTO> findSearchResultDTOByKeyword(
//            List<String> sidoList, List<String> sggList, List<String> emdList, MemberGatherArticleRole role, String keyword);

    Optional<GatherArticleResponse.StatusDTO> findStatusDTOById(Long gatherArticleId);

    List<Long> findGatherArticleIdsByUsername(String username);

}