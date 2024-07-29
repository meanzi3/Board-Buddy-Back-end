package sumcoda.boardbuddy.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom{
}
