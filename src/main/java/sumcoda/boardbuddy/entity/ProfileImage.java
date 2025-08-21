package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 원본 파일명
    @Column(nullable = false)
    private String originalFileName;

    // UUID 를 활용하여 구성된 S3에 저장되는 이름
    @Column(nullable = false)
    private String s3SavedObjectName;

    // 양방향 연관관계
    @OneToOne(mappedBy = "profileImage")
    private Member member;


    @Builder
    public ProfileImage(String originalFileName, String s3SavedObjectName) {
        this.originalFileName = originalFileName;
        this.s3SavedObjectName = s3SavedObjectName;
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static ProfileImage buildProfileImage(String originalFileName, String s3SavedObjectName) {
        return ProfileImage.builder()
                .originalFileName(originalFileName)
                .s3SavedObjectName(s3SavedObjectName)
                .build();
    }

    // ProfileImage 1 <-> 1 Member
    // 양방향 연관 관계 비공개 편의 메서드
    void assignMember(Member member, boolean updateInverseSide) {
        if (this.member == member) return;

        Member old = this.member;
        this.member = member;

        if (!updateInverseSide) return;

        // 연관관계 끊기
        if (old != null && old.getProfileImage() == this) {
            old.assignProfileImage(null, false);
        }
        // 연관관계 연결
        if (member != null && member.getProfileImage() != this) {
            member.assignProfileImage(this, false);
        }
    }
}
