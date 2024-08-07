package sumcoda.boardbuddy.repository.memberGatherArticle;

import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.dto.ReviewResponse;

import java.util.List;
import java.util.Optional;

public interface MemberGatherArticleRepositoryCustom {

  boolean isAuthor(Long gatherArticleId, Long memberId);

  boolean isHasRole(Long gatherArticleId, String username);

  Optional<MemberResponse.UsernameDTO> findAuthorUsernameByGatherArticleId(Long gatherArticleId);

  List<MemberResponse.UsernameDTO> findParticipantsByGatherArticleId(Long gatherArticleId);

  List<ReviewResponse.ReviewDTO> findParticipantsExcludingUsername(Long gatherArticleId, String username);
}
