package sumcoda.boardbuddy.util;


import sumcoda.boardbuddy.dto.BadgeImageInfoDTO;
import sumcoda.boardbuddy.dto.MemberProfileInfoDTO;
import sumcoda.boardbuddy.dto.fetch.MemberProfileProjection;

import java.util.List;
import java.util.UUID;

public final class ProfileImageUtil {

    private static final String PROFILE_IMAGE_PREFIX = "prod/images/profiles/";


    private ProfileImageUtil() {
        /* 유틸 클래스라 인스턴스화 금지 */
    }

    /**
     * Projection 객체를 BadgeImageInfoDTO 객체로 변환
     *
     * @param projectionDTO DB에서 조회된 BadgeImageInfoProjectionDTO 리스트
     * @param signedUrl CloudFront Signed URL 생성 서비스
     * @return BadgeImageInfoDTO 객체
     */
    public static MemberProfileInfoDTO convertBadgeImageInfoDTO(MemberProfileProjection projectionDTO, List<BadgeImageInfoDTO> badgeImageInfoDTOList, String signedUrl) {
        return MemberProfileInfoDTO.builder()
                .profileImageSignedURL(signedUrl)
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
     * 주어진 프로필 이미지 이름으로 프로필 이미지 요청 키를 생성
     *
     * @param s3SavedObjectName S3에 저장된 프로필 이미지 이름
     * @return "prod/images/profile/UUID.png" 같은 완전한 파일 경로
     */
    public static String buildProfileImageS3RequestKey(String s3SavedObjectName) {

        return PROFILE_IMAGE_PREFIX + s3SavedObjectName;
    }

    // S3에 저장할 파일 이름 생성
    public static String buildS3SavedObjectName(String originalFilename) {

        String ext = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        }

        // 2. UUID + 확장자 조합
        return UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
    }



}
