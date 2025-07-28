package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BadgeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 원본 파일명
    @Column(nullable = false)
    private String s3SavedObjectName;

    // 뱃지 발급 연월 정보
    @Column(nullable = false)
    private String badgeYearMonth;

    // 연관관계 주인
    // 양방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public BadgeImage(String s3SavedObjectName, String badgeYearMonth, Member member) {
        this.s3SavedObjectName = s3SavedObjectName;
        this.badgeYearMonth = badgeYearMonth;
        this.assignMember(member);
    }

    // 직접 빌더 패턴의 생성자를 활용하지 않고 해당 메서드를 활용하여 엔티티 생성
    public static BadgeImage buildBadgeImage(String s3SavedObjectName, String badgeYearMonth, Member member) {
        return BadgeImage.builder()
                .s3SavedObjectName(s3SavedObjectName)
                .badgeYearMonth(badgeYearMonth)
                .member(member)
                .build();
    }

    // BadgeImage N <-> 1 Member
    // 양방향 연관관계 편의 메서드
    public void assignMember(Member member) {
        if (this.member != null) {
            this.member.getBadgeImages().remove(this);
        }
        this.member = member;

        if (!member.getBadgeImages().contains(this)) {
            member.addBadgeImage(this);
        }
    }
}
