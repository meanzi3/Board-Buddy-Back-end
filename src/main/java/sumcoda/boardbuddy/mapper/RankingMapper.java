package sumcoda.boardbuddy.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.client.MemberRankingDTO;
import sumcoda.boardbuddy.dto.fetch.MemberRankingProjection;
import sumcoda.boardbuddy.generator.CloudFrontSignedUrlGenerator;

import java.util.List;


@Component
@RequiredArgsConstructor
public final class RankingMapper {

    private final CloudFrontSignedUrlGenerator cloudFrontSignedUrlGenerator;

    /**
     * Projection 리스트를 MemberRankingDTO 객체로 변환
     *
     * @param projections DB에서 조회된 MemberRankingProjection 리스트
     * @return MemberRankingDTO 리스트
     */
    public List<MemberRankingDTO> toMemberRankingDTOList(List<MemberRankingProjection> projections) {
        return projections.stream().map(projection -> {

            // 프로필 이미지 S3 키 → CloudFront Signed URL 생성
            String profileImageSignedURL = cloudFrontSignedUrlGenerator.generateProfileImageSignedUrl(projection.s3SavedObjectName());

            return MemberRankingDTO.builder()
                    .nickname(projection.nickname())
                    .profileImageSignedURL(profileImageSignedURL)
                    .build();
        }).toList();
    }
}