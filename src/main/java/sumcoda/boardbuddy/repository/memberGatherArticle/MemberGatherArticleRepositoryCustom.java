package sumcoda.boardbuddy.repository.memberGatherArticle;

import sumcoda.boardbuddy.dto.MemberResponse;

import java.util.Optional;


public interface MemberGatherArticleRepositoryCustom {

  boolean isAuthor(Long gatherArticleId, Long memberId);

  boolean isHasRole(Long gatherArticleId, String username);

  Optional<MemberResponse.UserNameDTO> findAuthorUsernameByGatherArticleId(Long gatherArticleId);
}
