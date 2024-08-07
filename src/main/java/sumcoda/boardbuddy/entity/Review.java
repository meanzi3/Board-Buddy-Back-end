package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 리뷰 전송 여부
    private Boolean hasReviewed;

    // 연관관계 주인
    // 양방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private Member reviewer;

    // 연관관계 주인
    // 양방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id")
    private Member reviewee;

    // 연관관계 주인
    // 양방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gather_article_id")
    private GatherArticle gatherArticle;

    @Builder
    public Review(Member reviewer, Member reviewee, Boolean hasReviewed, GatherArticle gatherArticle) {
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.hasReviewed = hasReviewed;
        this.gatherArticle = gatherArticle;
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static Review buildReview(Member reviewer, Member reviewee, GatherArticle gatherArticle, Boolean hasReviewed) {
        return Review.builder()
                .reviewer(reviewer)
                .reviewee(reviewee)
                .gatherArticle(gatherArticle)
                .hasReviewed(hasReviewed)
                .build();
    }

    // Review N <-> 1 Member(reviewer)
    // 양방향 연관관계 편의 메서드
    public void assignReviewer(Member reviewer) {
        if (this.reviewer != null) {
            this.reviewer.getSentReviews().remove(this);
        }
        this.reviewer = reviewer;
        if (!reviewer.getSentReviews().contains(this)) {
            reviewer.addSentReview(this);
        }
    }

    // Review N <-> 1 Member(reviewee)
    // 양방향 연관관계 편의 메서드
    public void assignReviewee(Member reviewee) {
        if (this.reviewee != null) {
            this.reviewee.getReceiveReviews().remove(this);
        }
        this.reviewee = reviewee;
        if (!reviewee.getReceiveReviews().contains(this)) {
            reviewee.addReceiveReview(this);
        }
    }

    // Review N <-> 1 GatherArticle
    // 양방향 연관관계 편의 메서드
    public void assignGatherArticle(GatherArticle gatherArticle) {
        if (this.gatherArticle != null) {
            this.gatherArticle.getReviews().remove(this);
        }
        this.gatherArticle = gatherArticle;

        if (!gatherArticle.getReviews().contains(this)) {
            gatherArticle.addReview(this);
        }
    }
}
