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
    private String originalFilename;

    // UUID 를 활용하여 구성된 파일 이름
    @Column(nullable = false)
    private String savedFilename;

    // 이미지에 대한 URL 정보를 DB에서 찾을때 활용
    @Column(nullable = false)
    private String profileImageS3SavedURL;

    @Builder
    public ProfileImage(String originalFilename, String savedFilename, String profileImageS3SavedURL) {
        this.originalFilename = originalFilename;
        this.savedFilename = savedFilename;
        this.profileImageS3SavedURL = profileImageS3SavedURL;
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static ProfileImage buildProfileImage(String originalFilename, String savedFilename, String profileImageS3SavedURL) {
        return ProfileImage.builder()
                .originalFilename(originalFilename)
                .savedFilename(savedFilename)
                .profileImageS3SavedURL(profileImageS3SavedURL)
                .build();
    }
}
