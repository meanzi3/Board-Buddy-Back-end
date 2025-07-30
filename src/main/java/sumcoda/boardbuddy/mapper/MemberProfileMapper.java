package sumcoda.boardbuddy.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.client.BadgeImageInfoDTO;
import sumcoda.boardbuddy.dto.client.MemberAuthProfileDTO;
import sumcoda.boardbuddy.dto.client.MemberProfileInfoDTO;
import sumcoda.boardbuddy.dto.fetch.MemberAuthProfileProjection;
import sumcoda.boardbuddy.dto.fetch.MemberProfileProjection;
import sumcoda.boardbuddy.generator.CloudFrontSignedUrlGenerator;

import java.util.List;


@Component
@RequiredArgsConstructor
public class MemberProfileMapper {

    private final CloudFrontSignedUrlGenerator cloudFrontSignedUrlGenerator;

    /**
     * Projection 객체를 MemberProfileInfoDTO 객체로 변환
     *
     * @param projection DB에서 조회된 MemberProfileProjection 객체
     * @param badgeImageInfoDTOList 클라이언트에게 전달하기위해 가공된  BadgeImageInfoDTO 리스트
     * @return MemberProfileInfoDTO 객체
     */
    public MemberProfileInfoDTO toMemberProfileInfoDTO(MemberProfileProjection projection, List<BadgeImageInfoDTO> badgeImageInfoDTOList) {

        // 프로필 이미지 S3 키 → CloudFront Signed URL 생성
        String profileImageSignedURL = cloudFrontSignedUrlGenerator.generateProfileImageSignedUrl(projection.s3SavedObjectName());

        return MemberProfileInfoDTO.builder()
                .profileImageSignedURL(profileImageSignedURL)
                .description(projection.description())
                .rank(projection.rank())
                .buddyScore(projection.buddyScore())
                .badges(badgeImageInfoDTOList)
                .joinCount(projection.joinCount())
                .totalExcellentCount(projection.totalExcellentCount())
                .totalGoodCount(projection.totalGoodCount())
                .totalBadCount(projection.totalBadCount())
                .build();
    }

    /**
     * Projection 객체를 MemberAuthProfileDTO 객체로 변환
     *
     * @param projection DB에서 조회된 MemberAuthProfileProjection 객체
     * @return MemberAuthProfileDTO 객체
     */
    public MemberAuthProfileDTO toMemberAuthProfileDTO(MemberAuthProfileProjection projection) {

        // 프로필 이미지 S3 키 → CloudFront Signed URL 생성
        String profileImageSignedURL = cloudFrontSignedUrlGenerator.generateProfileImageSignedUrl(projection.s3SavedObjectName());

        return MemberAuthProfileDTO.builder()
                .nickname(projection.nickname())
                .isPhoneNumberVerified(projection.phoneNumber() != null)
                .memberType(projection.memberType())
                .profileImageSignedURL(profileImageSignedURL)
                .build();
    }
}
