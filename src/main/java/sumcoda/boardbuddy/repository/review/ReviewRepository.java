package sumcoda.boardbuddy.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.GatherArticle;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByReviewerAndRevieweeAndGatherArticle(Member reviewer, Member reviewee, GatherArticle gatherArticle);
}
