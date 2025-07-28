package sumcoda.boardbuddy.util;

import sumcoda.boardbuddy.dto.MemberRankingDTO;
import sumcoda.boardbuddy.dto.fetch.MemberRankingProjection;

public final class RankingUtil {

    public static final int TOP_RANK_COUNT = 3;

    private RankingUtil() {
        /* 유틸 클래스라 인스턴스화 금지 */
    }

    /**
     * Projection 객체를 MemberRankingDTO 객체로 변환
     *
     * @param projection DB에서 조회된 MemberRankingProjection 객체
     * @param profileImageSignedURL 생성된 CloudFront Signed URL
     * @return MemberRankingDTO 객체
     */
    public static MemberRankingDTO convertMemberRankingDTO(MemberRankingProjection projection, String profileImageSignedURL) {
        return MemberRankingDTO.builder()
                .nickname(projection.nickname())
                .profileImageSignedURL(profileImageSignedURL)
                .build();
    }
}