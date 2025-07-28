package sumcoda.boardbuddy.util;

import sumcoda.boardbuddy.dto.BadgeImageInfoDTO;
import sumcoda.boardbuddy.dto.MemberAuthProfileDTO;
import sumcoda.boardbuddy.dto.MemberProfileInfoDTO;
import sumcoda.boardbuddy.dto.fetch.MemberAuthProfileProjection;
import sumcoda.boardbuddy.dto.fetch.MemberProfileProjection;

import java.util.List;

public final class MemberProfileUtil {

    private MemberProfileUtil() {
        /* 유틸 클래스라 인스턴스화 금지 */
    }

    /**
     * Projection 객체를 MemberProfileInfoDTO 객체로 변환
     *
     * @param projectionDTO DB에서 조회된 MemberProfileProjection 객체
     * @param badgeImageInfoDTOList 클라이언트에게 전달하기위해 가공된  BadgeImageInfoDTO 리스트
     * @param profileImageSignedURL 생성된 CloudFront Signed URL
     * @return MemberProfileInfoDTO 객체
     */
    public static MemberProfileInfoDTO convertMemberProfileInfoDTO(MemberProfileProjection projectionDTO, List<BadgeImageInfoDTO> badgeImageInfoDTOList, String profileImageSignedURL) {
        return MemberProfileInfoDTO.builder()
                .profileImageSignedURL(profileImageSignedURL)
                .description(projectionDTO.description())
                .rank(projectionDTO.rank())
                .buddyScore(projectionDTO.buddyScore())
                .badges(badgeImageInfoDTOList)
                .joinCount(projectionDTO.joinCount())
                .totalExcellentCount(projectionDTO.totalExcellentCount())
                .totalGoodCount(projectionDTO.totalGoodCount())
                .totalBadCount(projectionDTO.totalBadCount())
                .build();
    }

    /**
     * Projection 객체를 MemberAuthProfileDTO 객체로 변환
     *
     * @param projection DB에서 조회된 MemberAuthProfileProjection 객체
     * @param profileImageSignedURL 생성된 CloudFront Signed URL
     * @return MemberAuthProfileDTO 객체
     */
    public static MemberAuthProfileDTO convertMemberAuthProfileDTO(MemberAuthProfileProjection projection, String profileImageSignedURL) {
        return MemberAuthProfileDTO.builder()
                .nickname(projection.nickname())
                .isPhoneNumberVerified(projection.phoneNumber() != null)
                .memberType(projection.memberType())
                .profileImageSignedURL(profileImageSignedURL)
                .build();
    }
}
