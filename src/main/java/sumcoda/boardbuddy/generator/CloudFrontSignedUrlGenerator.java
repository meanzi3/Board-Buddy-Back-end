package sumcoda.boardbuddy.generator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.service.CloudFrontSignedUrlService;

import static sumcoda.boardbuddy.util.BadgeImageUtil.*;
import static sumcoda.boardbuddy.util.ProfileImageUtil.buildProfileImageS3RequestKey;

@Component
@RequiredArgsConstructor
public class CloudFrontSignedUrlGenerator {

    private final CloudFrontSignedUrlService cloudFrontSignedUrlService;

    /**
     * 프로필 이미지용 CloudFront Signed URL 생성
     *
     * @param s3SavedObjectName S3에 저장된 객체 이름
     * @return Signed URL, 입력이 null이면 null
     */
    public String generateProfileImageSignedUrl(String s3SavedObjectName) {
        if (s3SavedObjectName == null) {
            return null;
        }

        String requestKey = buildProfileImageS3RequestKey(s3SavedObjectName);

        return cloudFrontSignedUrlService.generateSignedUrl(requestKey);
    }

    /**
     * 배지 이미지용 CloudFront Signed URL 생성
     *
     * @param s3SavedObjectName S3에 저장된 객체 이름
     * @return Signed URL, 입력이 null이면 null
     */
    public String generateBadgeImageSignedUrl(String s3SavedObjectName) {
        if (s3SavedObjectName == null) {
            return null;
        }

        String requestKey = buildBadgeImageS3RequestKey(s3SavedObjectName);

        return cloudFrontSignedUrlService.generateSignedUrl(requestKey);
    }
}
