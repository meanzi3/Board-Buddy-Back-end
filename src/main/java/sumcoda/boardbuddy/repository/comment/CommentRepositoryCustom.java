package sumcoda.boardbuddy.repository.comment;

import sumcoda.boardbuddy.entity.Member;

import java.time.LocalDateTime;

public interface CommentRepositoryCustom {

  long countCommentsByMember(Member member, LocalDateTime startOfLastMonth, LocalDateTime endOfLastMonth);

}
