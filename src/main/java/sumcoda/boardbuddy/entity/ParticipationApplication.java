package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.ParticipationApplicationStatus;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ParticipationApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 참가 신청 거절 응답 횟수
    @Column(nullable = false)
    private Integer rejectedParticipationCount;

    // 모집글에 참가 신청하는 사용자의 상태를 나타내기위한 role
    // ex) NONE, PENDING, APPROVED, REJECTED, CANCELED
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipationApplicationStatus participationApplicationStatus;

    // 연관관계 주인
    // 양방향 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_gather_article_id")
    private MemberGatherArticle memberGatherArticle;

    @Builder
    public ParticipationApplication(Integer rejectedParticipationCount, ParticipationApplicationStatus participationApplicationStatus, MemberGatherArticle memberGatherArticle) {
        this.rejectedParticipationCount = rejectedParticipationCount;
        this.participationApplicationStatus = participationApplicationStatus;
        this.assignMemberGatherArticle(memberGatherArticle);

    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static ParticipationApplication buildParticipationApplication(Integer rejectedParticipationCount, ParticipationApplicationStatus participationApplicationStatus, MemberGatherArticle memberGatherArticle) {
        return ParticipationApplication.builder()
                .rejectedParticipationCount(rejectedParticipationCount)
                .participationApplicationStatus(participationApplicationStatus)
                .memberGatherArticle(memberGatherArticle)
                .build();
    }

    // ParticipationApplication 1 <-> 1 MemberGatherArticle
    // 양방향 연관관계 편의 메서드
    public void assignMemberGatherArticle(MemberGatherArticle memberGatherArticle) {
        if (this.memberGatherArticle != null) {
            this.memberGatherArticle.assignParticipationApplication(null);
        }
        this.memberGatherArticle = memberGatherArticle;
        if (memberGatherArticle != null && memberGatherArticle.getParticipationApplication() != this) {
            memberGatherArticle.assignParticipationApplication(this);
        }
    }



    // 모집글 참가신청에 대한 사용자의 상태 업데이트
    public void assignParticipationApplicationStatus(ParticipationApplicationStatus participationApplicationStatus) {
        this.participationApplicationStatus = participationApplicationStatus;
    }

    // 동일한 모집글에 대해 참가 거절 당한 횟수 업데이트
    public void assignRejectedParticipationCount(Integer rejectedParticipationCount) {
        this.rejectedParticipationCount = rejectedParticipationCount;
    }


}

