package sumcoda.boardbuddy.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.client.GatherArticleAuthorDTO;
import sumcoda.boardbuddy.dto.client.GatherArticleDetailedInfoDTO;
import sumcoda.boardbuddy.dto.fetch.GatherArticleAuthorProjection;
import sumcoda.boardbuddy.dto.fetch.GatherArticleDetailedInfoProjection;
import sumcoda.boardbuddy.generator.CloudFrontSignedUrlGenerator;


@Component
@RequiredArgsConstructor
public class GatherArticleMapper {

    private final CloudFrontSignedUrlGenerator cloudFrontSignedUrlGenerator;

    /**
     * Projection 객체를 GatherArticleDetailedInfoDTO 객체로 변환
     *
     * @param projection DB에서 조회된 GatherArticleDetailedInfoProjection 객체
     * @param authorDTO 클라이언트에게 전달하기위해 가공된 GatherArticleAuthorDTO 객체
     * @return GatherArticleDetailedInfoDTO 객체
     */
    public GatherArticleDetailedInfoDTO toGatherArticleDetailedInfoDTO(GatherArticleDetailedInfoProjection projection, GatherArticleAuthorDTO authorDTO) {
        return GatherArticleDetailedInfoDTO.builder()
                .title(projection.title())
                .description(projection.description())
                .author(authorDTO)
                .sido(projection.sido())
                .sgg(projection.sgg())
                .emd(projection.emd())
                .meetingLocation(projection.meetingLocation())
                .x(projection.x())
                .y(projection.y())
                .maxParticipants(projection.maxParticipants())
                .currentParticipants(projection.currentParticipants())
                .startDateTime(projection.startDateTime())
                .endDateTime(projection.endDateTime())
                .createdAt(projection.createdAt())
                .status(projection.gatherArticleStatus())
                .build();
    }

    /**
     * Projection 객체를 GatherArticleAuthorDTO 객체로 변환
     *
     * @param projection DB에서 조회된 GatherArticleAuthorProjection 객체
     * @return GatherArticleAuthorDTO 객체
     */
    public GatherArticleAuthorDTO toGatherArticleAuthorDTO(GatherArticleAuthorProjection projection) {

        // 프로필 이미지 S3 키 → CloudFront Signed URL 생성
        String profileImageSignedURL = cloudFrontSignedUrlGenerator.generateProfileImageSignedUrl(projection.s3SavedObjectName());

        return GatherArticleAuthorDTO.builder()
                .nickname(projection.nickname())
                .rank(projection.rank())
                .profileImageSignedURL(profileImageSignedURL)
                .description(projection.description())
                .build();
    }
}
