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
    private String originalFilename;

    // UUID 를 활용하여 구성된 파일 이름
    @Column(nullable = false)
    private String savedFilename;

    // 이미지에 대한 URL 정보를 DB에서 찾을때 활용
    @Column(nullable = false)
    private String awsS3SavedFileURL;

    // 연관관계 주인
    // 양방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public BadgeImage(String originalFilename, String savedFilename, String awsS3SavedFileURL, Member member) {
        this.originalFilename = originalFilename;
        this.savedFilename = savedFilename;
        this.awsS3SavedFileURL = awsS3SavedFileURL;
        this.member = member;
    }

    // 직접 빌더 패턴의 생성자를 활용하지 않고 해당 메서드를 활용하여 엔티티 생성
    public static BadgeImage createBadgeImage(String originalFilename, String savedFilename, String awsS3SavedFileURL, Member member) {
        return BadgeImage.builder()
                .originalFilename(originalFilename)
                .savedFilename(savedFilename)
                .awsS3SavedFileURL(awsS3SavedFileURL)
                .member(member)
                .build();
    }
}
