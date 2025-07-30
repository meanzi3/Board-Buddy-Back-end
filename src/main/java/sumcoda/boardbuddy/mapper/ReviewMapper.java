package sumcoda.boardbuddy.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.client.ReviewAuthorDTO;
import sumcoda.boardbuddy.dto.fetch.ReviewAuthorProjection;
import sumcoda.boardbuddy.generator.CloudFrontSignedUrlGenerator;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ReviewMapper {

    private final CloudFrontSignedUrlGenerator cloudFrontSignedUrlGenerator;

    /**
     * Projection 리스트를 ReviewAuthorDTO 객체로 변환
     *
     * @param projections DB에서 조회된 ReviewAuthorProjection 리스트
     * @return ReviewAuthorDTO 리스트
     */
    public List<ReviewAuthorDTO> toReviewAuthorDTOList(List<ReviewAuthorProjection> projections) {
        return projections.stream().map(projection -> {

            // 프로필 이미지 S3 키 → CloudFront Signed URL 생성
            String profileImageSignedURL = cloudFrontSignedUrlGenerator.generateProfileImageSignedUrl(projection.s3SavedObjectName());

            return ReviewAuthorDTO.builder()
                    .profileImageSignedURL(profileImageSignedURL)
                    .rank(projection.rank())
                    .nickname(projection.nickname())
                    .hasReviewed(projection.hasReviewed())
                    .build();
        }).toList();
    }
}