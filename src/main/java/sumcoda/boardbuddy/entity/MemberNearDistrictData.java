package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberNearDistrictData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 주변의 oo시, oo도
    @Column(nullable = false)
    private String sido;

    // 사용자 주변의 oo시, oo구
    @Column(nullable = false)
    private String sigu;

    // 사용자 주변의 00동
    @Column(nullable = false)
    private String dong;

    // 반경 정보
    @Column(nullable = false)
    private Integer radius;

    // 양방향 연관관계
    // 연관관계 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public MemberNearDistrictData(String sido, String sigu, String dong, Integer radius, Member member) {
        this.sido = sido;
        this.sigu = sigu;
        this.dong = dong;
        this.radius = radius;
        this.assignMember(member);
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static MemberNearDistrictData createMemberNearDistrict(String sido, String sigu, String dong, Integer radius, Member member) {
        return MemberNearDistrictData.builder()
                .sido(sido)
                .sigu(sigu)
                .dong(dong)
                .radius(radius)
                .member(member)
                .build();
    }

    // MemberNearDistrict N <-> 1 Member
    // 양방향 연관관계 편의 메서드
    public void assignMember(Member member) {
        if (this.member != null) {
            this.member.getMemberNearDistricts().remove(this);
        }
        this.member = member;

        if (!member.getMemberNearDistricts().contains(this)) {
            member.addMemberNearDistrict(this);
        }
    }
}
