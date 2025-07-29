package sumcoda.boardbuddy.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.client.BadgeImageInfoDTO;
import sumcoda.boardbuddy.dto.fetch.BadgeImageInfoProjection;
import sumcoda.boardbuddy.generator.CloudFrontSignedUrlGenerator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BadgeImageMapper {

    private final CloudFrontSignedUrlGenerator cloudFrontSignedUrlGenerator;


    /**
     * Projection 리스트를 BadgeImageInfoDTO 리스트로 변환
     *
     * @param projections DB에서 조회된 BadgeImageInfoProjection 리스트
     * @return BadgeImageInfoDTO 리스트
     */
    public List<BadgeImageInfoDTO> toBadgeImageInfoDTOList(List<BadgeImageInfoProjection> projections) {
        return projections.stream()
                .map(projection -> {

                    // 뱃지 이미지 S3 키 → CloudFront Signed URL 생성
                    String badgeImageSignedURL = cloudFrontSignedUrlGenerator.generateBadgeImageSignedUrl(projection.s3SavedObjectName());

                    return BadgeImageInfoDTO.builder()
                            .badgeImageSignedURL(badgeImageSignedURL)
                            .badgeYearMonth(projection.badgeYearMonth())
                            .build();
                })
                .toList();
    }
}
