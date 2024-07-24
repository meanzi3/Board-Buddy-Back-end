package sumcoda.boardbuddy.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.entity.Member;

import java.time.LocalDateTime;

import static sumcoda.boardbuddy.entity.QComment.*;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

  private final JPAQueryFactory jpaQueryFactory;

  // 지난 달에 쓴 댓글 갯수 세기
  @Override
  public long countCommentsByMember(Member member, LocalDateTime startOfLastMonth, LocalDateTime endOfLastMonth) {
    return jpaQueryFactory.select(comment.count())
            .from(comment)
            .where(comment.member.eq(member)
                    .and(comment.createdAt.between(startOfLastMonth, endOfLastMonth)))
            .fetchOne();
  }

}
