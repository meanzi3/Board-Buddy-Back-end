package sumcoda.boardbuddy.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import sumcoda.boardbuddy.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom{
}
