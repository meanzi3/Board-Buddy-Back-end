package sumcoda.boardbuddy.repository.memberGatherArticle;

import sumcoda.boardbuddy.entity.GatherArticle;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.entity.MemberGatherArticle;


public interface MemberGatherArticleRepositoryCustom {

  boolean isAuthor(Long gatherArticleId, Long memberId);

  boolean isPermit(Long gatherArticleId, String username);
}
