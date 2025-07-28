package sumcoda.boardbuddy.util;

import sumcoda.boardbuddy.dto.GatherArticleAuthorDTO;
import sumcoda.boardbuddy.dto.GatherArticleDetailedInfoDTO;
import sumcoda.boardbuddy.dto.fetch.GatherArticleAuthorProjection;
import sumcoda.boardbuddy.dto.fetch.GatherArticleDetailedInfoProjection;

public final class GatherArticleUtil {

    public static final int PAGE_SIZE = 15;

    public static final int GATHER_ARTICLE_MINIMUM_SEARCH_LENGTH = 2;

    private GatherArticleUtil() {
        /* 유틸 클래스라 인스턴스화 금지 */
    }

    /**
     * Projection 객체를 GatherArticleDetailedInfoDTO 객체로 변환
     *
     * @param projection DB에서 조회된 GatherArticleDetailedInfoProjection 객체
     * @param authorDTO 클라이언트에게 전달하기위해 가공된 GatherArticleAuthorDTO 객체
     * @return GatherArticleDetailedInfoDTO 객체
     */
    public static GatherArticleDetailedInfoDTO convertGatherArticleDetailedInfoDTO(GatherArticleDetailedInfoProjection projection, GatherArticleAuthorDTO authorDTO) {
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
     * @param profileImageSignedURL 생성된 CloudFront Signed URL
     * @return GatherArticleAuthorDTO 객체
     */
    public static GatherArticleAuthorDTO convertGatherArticleAuthorDTO(GatherArticleAuthorProjection projection, String profileImageSignedURL) {
        return GatherArticleAuthorDTO.builder()
                .nickname(projection.nickname())
                .rank(projection.rank())
                .profileImageSignedURL(profileImageSignedURL)
                .description(projection.description())
                .build();
    }
}
