package sumcoda.boardbuddy.repository.gatherArticle;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.dto.fetch.GatherArticleAuthorProjection;
import sumcoda.boardbuddy.dto.fetch.GatherArticleDetailedInfoProjection;
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

    /**
     * @apiNote V1 - 내 동네 반경 n km 기반 모집글 리스트 조회 (현재 미사용, 향후 복원 가능)
     */
//    Slice<GatherArticleResponse.ReadSliceDTO> findReadSliceDTOByLocationAndStatusAndSort(
//            List<String> sidoList, List<String> sggList, List<String> emdList,
//            String status, String sort, MemberGatherArticleRole role, Pageable pageable);

    /**
     * @apiNote V2 - 사용자가 지정한 지역 기반 모집글 리스트 조회
     */
    Slice<GatherArticleResponse.ReadSliceDTO> findReadSliceDTOByLocationV2AndStatusAndSortAndKeyword(
            String sido, String sgg,
            String status, String sort, String keyword, MemberGatherArticleRole role, Pageable pageable);

    Optional<GatherArticleResponse.SummaryInfoDTO> findSimpleInfoByGatherArticleId(Long gatherArticleId);

    Optional<GatherArticleDetailedInfoProjection> findGatherArticleDetailedInfoByGatherArticleId(Long gatherArticleId);

    Optional<GatherArticleAuthorProjection> findGatherArticleAuthorByGatherArticleId(Long gatherArticleId);

    Optional<GatherArticleResponse.ParticipationApplicationStatusDTO> findParticipationApplicationStatusDTOByGatherArticleIdAndUsername(Long gatherArticleId, String username);

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