package sumcoda.boardbuddy.util;

import sumcoda.boardbuddy.dto.ParticipationApplicationInfoDTO;
import sumcoda.boardbuddy.dto.fetch.ParticipationApplicationInfoProjection;

public final class ParticipationApplicationUtil {

    private ParticipationApplicationUtil() {
        /* 유틸 클래스라 인스턴스화 금지 */
    }

    /**
     * Projection 객체를 ParticipationApplicationInfoDTO 객체로 변환
     *
     * @param projection DB에서 조회된 ParticipationApplicationInfoProjection 객체
     * @param profileImageSignedURL 생성된 CloudFront Signed URL
     * @return ParticipationApplicationInfoDTO 객체
     */
    public static ParticipationApplicationInfoDTO convertParticipationApplicationInfoDTO(ParticipationApplicationInfoProjection projection, String profileImageSignedURL) {
        return ParticipationApplicationInfoDTO.builder()
                .id(projection.id())
                .nickname(projection.nickname())
                .profileImageSignedURL(profileImageSignedURL)
                .build();
    }
}
