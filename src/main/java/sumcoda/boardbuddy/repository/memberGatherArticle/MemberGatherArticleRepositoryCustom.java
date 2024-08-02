package sumcoda.boardbuddy.repository.memberGatherArticle;

import java.util.Optional;


public interface MemberGatherArticleRepositoryCustom {

  boolean isAuthor(Long gatherArticleId, Long memberId);

  boolean isHasRole(Long gatherArticleId, String username);

  Optional<String> findAuthorUsernameByGatherArticleId(Long gatherArticleId);
}
