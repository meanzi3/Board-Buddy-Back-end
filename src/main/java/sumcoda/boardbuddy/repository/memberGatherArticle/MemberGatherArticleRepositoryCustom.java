package sumcoda.boardbuddy.repository.memberGatherArticle;

import sumcoda.boardbuddy.entity.GatherArticle;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.entity.MemberGatherArticle;


public interface MemberGatherArticleRepositoryCustom {
  Member findAuthorByGatherArticleId(Long gatherArticleId);

  boolean isAuthor(Long gatherArticleId, Long memberId);

  MemberGatherArticle findByGatherArticleAndMember(GatherArticle gatherArticle, Member member);
}
