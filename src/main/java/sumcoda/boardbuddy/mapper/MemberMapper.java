package sumcoda.boardbuddy.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sumcoda.boardbuddy.dto.client.BadgeImageInfoDTO;
import sumcoda.boardbuddy.dto.client.MemberDetailDTO;
import sumcoda.boardbuddy.dto.client.MemberSummaryDTO;
import sumcoda.boardbuddy.dto.fetch.MemberSummaryProjection;
import sumcoda.boardbuddy.dto.fetch.MemberDetailProjection;
import sumcoda.boardbuddy.generator.CloudFrontSignedUrlGenerator;

import java.util.List;


@Component
@RequiredArgsConstructor
public class MemberMapper {

    private final CloudFrontSignedUrlGenerator cloudFrontSignedUrlGenerator;

    /**
     * Projection 객체를 MemberDetailDTO 객체로 변환
     *
     * @param projection DB에서 조회된 MemberDetailProjection 객체
     * @param badgeImageInfoDTOList 클라이언트에게 전달하기위해 가공된  BadgeImageInfoDTO 리스트
     * @return MemberDetailDTO 객체
     */
    public MemberDetailDTO toMemberDetailDTO(MemberDetailProjection projection, List<BadgeImageInfoDTO> badgeImageInfoDTOList) {

        // 프로필 이미지 S3 키 → CloudFront Signed URL 생성
        String profileImageSignedURL = cloudFrontSignedUrlGenerator.generateProfileImageSignedUrl(projection.s3SavedObjectName());

        return MemberDetailDTO.builder()
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
     * Projection 객체를 MemberSummaryDTO 객체로 변환
     *
     * @param projection DB에서 조회된 MemberSummaryProjection 객체
     * @return MemberSummaryDTO 객체
     */
    public MemberSummaryDTO toMemberSummaryDTO(MemberSummaryProjection projection) {

        // 프로필 이미지 S3 키 → CloudFront Signed URL 생성
        String profileImageSignedURL = cloudFrontSignedUrlGenerator.generateProfileImageSignedUrl(projection.s3SavedObjectName());

        return MemberSummaryDTO.builder()
                .nickname(projection.nickname())
                .isPhoneNumberVerified(projection.phoneNumber() != null)
                .memberType(projection.memberType())
                .profileImageSignedURL(profileImageSignedURL)
                .build();
    }
}
