package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.MemberGatherArticleRole;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberGatherArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 해당 유저가 방에 참석한 시점
    private LocalDateTime joinedAt;

    // 모집글 참여자의 권한을 나타내기위한 role
    // ex) AUTHOR, PARTICIPANT
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberGatherArticleRole memberGatherArticleRole;

    // 각 참가자의 노쇼예요 횟수를 나타내기 위한 카운트
    @Column(nullable = false)
    private Integer receiveNoShowCount;

    // 양방향 연관관계
    // 연관관계 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 양방향 연관관계
    // 연관관계 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gather_article_id")
    private GatherArticle gatherArticle;

    // 양방향 연관관계
    @OneToOne(mappedBy = "memberGatherArticle")
    private ParticipationApplication participationApplication;

    @Builder
    public MemberGatherArticle(LocalDateTime joinedAt, MemberGatherArticleRole memberGatherArticleRole, Integer receiveNoShowCount, Member member, GatherArticle gatherArticle) {
        this.joinedAt = joinedAt;
        this.memberGatherArticleRole = memberGatherArticleRole;
        this.receiveNoShowCount = receiveNoShowCount;
        this.assignMember(member);
        this.assignGatherArticle(gatherArticle);
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static MemberGatherArticle buildMemberGatherArticle(LocalDateTime joinedAt, MemberGatherArticleRole memberGatherArticleRole, Integer receiveNoShowCount, Member member, GatherArticle gatherArticle) {
        return MemberGatherArticle.builder()
                .joinedAt(joinedAt)
                .memberGatherArticleRole(memberGatherArticleRole)
                .receiveNoShowCount(receiveNoShowCount)
                .member(member)
                .gatherArticle(gatherArticle)
                .build();
    }

    // MemberGatherArticle N <-> 1 Member
    // 양방향 연관관계 편의 메서드
    public void assignMember(Member member) {
        if (this.member != null) {
            this.member.getMemberGatherArticles().remove(this);
        }
        this.member = member;

        if (!member.getMemberGatherArticles().contains(this)) {
            member.addMemberGatherArticle(this);
        }
    }

    // MemberGatherArticle N <-> 1 GatherArticle
    // 양방향 연관관계 편의 메서드
    public void assignGatherArticle(GatherArticle gatherArticle) {
        if (this.gatherArticle != null) {
            this.gatherArticle.getMemberGatherArticles().remove(this);
        }
        this.gatherArticle = gatherArticle;

        if (!gatherArticle.getMemberGatherArticles().contains(this)) {
            gatherArticle.addMemberGatherArticle(this);
        }
    }

    // MemberGatherArticle 1 <-> 1 ParticipationApplication
    // 양방향 연관관계 편의 메서드
    public void assignParticipationApplication(ParticipationApplication participationApplication) {
        if (this.participationApplication != null) {
            this.participationApplication.assignMemberGatherArticle(null);
        }
        this.participationApplication = participationApplication;
        if (participationApplication != null && participationApplication.getMemberGatherArticle() != this) {
            participationApplication.assignMemberGatherArticle(this);
        }
    }

    // 사용자가 모집글에 참여된 시점 업데이트
    public void assignJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    // 모집글 관련 사용자의 권한 업데이트
    public void assignMemberGatherArticleRole(MemberGatherArticleRole memberGatherArticleRole) {
        this.memberGatherArticleRole = memberGatherArticleRole;
    }

    // 각 참가자에 대한 노쇼예요 횟수 업데이트
    public void assignReceiveNoShowCount(Integer receiveNoShowCount) {
        this.receiveNoShowCount = receiveNoShowCount;
    }

}
