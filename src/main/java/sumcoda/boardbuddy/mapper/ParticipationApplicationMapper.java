package sumcoda.boardbuddy.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.client.ParticipationApplicationInfoDTO;
import sumcoda.boardbuddy.dto.fetch.ParticipationApplicationInfoProjection;
import sumcoda.boardbuddy.generator.CloudFrontSignedUrlGenerator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ParticipationApplicationMapper {

    private final CloudFrontSignedUrlGenerator cloudFrontSignedUrlGenerator;

    /**
     * Projection 리스트를 ParticipationApplicationInfoDTO 리스트로 변환
     *
     * @param projections DB에서 조회된 ParticipationApplicationInfoProjection 리스트
     * @return ParticipationApplicationInfoDTO 리스트
     */
    public List<ParticipationApplicationInfoDTO> toParticipationApplicationInfoDTOList(List<ParticipationApplicationInfoProjection> projections) {
        return projections.stream().map(projection -> {

            // 프로필 이미지 S3 키 → CloudFront Signed URL 생성
            String profileImageSignedURL = cloudFrontSignedUrlGenerator.generateProfileImageSignedUrl(projection.s3SavedObjectName());

            return ParticipationApplicationInfoDTO.builder()
                            .id(projection.id())
                            .nickname(projection.nickname())
                            .profileImageSignedURL(profileImageSignedURL)
                            .build();
        }).toList();
    }
}
