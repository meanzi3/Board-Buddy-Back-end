package sumcoda.boardbuddy.repository.memberGatherArticle;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.enumerate.MemberGatherArticleRole;

import static sumcoda.boardbuddy.entity.QMemberGatherArticle.memberGatherArticle;

@RequiredArgsConstructor
public class MemberGatherArticleRepositoryCustomImpl implements MemberGatherArticleRepositoryCustom {
  private final JPAQueryFactory jpaQueryFactory;

  // 모집글의 작성자인지 확인
  @Override
  public boolean isAuthor(Long gatherArticleId, Long memberId) {
    Long count = jpaQueryFactory
            .select(memberGatherArticle.count())
            .from(memberGatherArticle)
            .where(memberGatherArticle.gatherArticle.id.eq(gatherArticleId)
                    .and(memberGatherArticle.member.id.eq(memberId))
                    .and(memberGatherArticle.memberGatherArticleRole.eq(MemberGatherArticleRole.AUTHOR)))
            .fetchOne();

    return count != null && count > 0;
  }

  // 유저와 모집글 사이에 isPermit 확인
  @Override
  public boolean isPermit(Long gatherArticleId, String username) {
    Long count = jpaQueryFactory
            .select(memberGatherArticle.count())
            .from(memberGatherArticle)
            .where(memberGatherArticle.gatherArticle.id.eq(gatherArticleId)
                    .and(memberGatherArticle.member.username.eq(username))
                    .and(memberGatherArticle.memberGatherArticleRole.eq(MemberGatherArticleRole.PARTICIPANT)))
            .fetchOne();

    return count != null && count > 0;
  }
}
