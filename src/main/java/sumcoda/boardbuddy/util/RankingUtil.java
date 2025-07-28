package sumcoda.boardbuddy.util;

import sumcoda.boardbuddy.dto.MemberRankingDTO;
import sumcoda.boardbuddy.dto.fetch.MemberRankingProjection;

public final class RankingUtil {

    public static final int TOP_RANK_COUNT = 3;

    private RankingUtil() {
        /* 유틸 클래스라 인스턴스화 금지 */
    }

    public static MemberRankingDTO convertMemberRankingDTO(MemberRankingProjection projection, String profileImageSignedURL) {
        return MemberRankingDTO.builder()
                .nickname(projection.nickname())
                .profileImageSignedURL(profileImageSignedURL)
                .build();
    }
}