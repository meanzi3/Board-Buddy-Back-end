package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.BadgeImageInfoDTO;
import sumcoda.boardbuddy.entity.BadgeImage;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.repository.member.MemberRepository;
import sumcoda.boardbuddy.repository.badgeImage.BadgeImageRepository;
import sumcoda.boardbuddy.util.FileStorageUtil;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

import static sumcoda.boardbuddy.util.BadgeImageUtil.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BadgeImageService {

    private final BadgeImageRepository badgeImageRepository;

    private final MemberRepository memberRepository;

    private final CloudFrontSignedUrlService cloudFrontSignedUrlService;

    /**
     * 뱃지 조회 요청 캐치
     *
     * @param nickname 사용자가 입력한 닉네임
     * @return 뱃지 이미지 URL 리스트
     **/
    public List<BadgeImageInfoDTO> getBadges(String nickname) {

        if (nickname == null) {
            throw new MemberRetrievalException("뱃지 조회 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
        }

        if (Boolean.FALSE.equals(memberRepository.existsByNickname(nickname))) {
            throw new MemberNotFoundException("해당 유저를 찾을 수 없습니다.");
        }

        // 로컬 환경용 코드
//        return badgeImageRepository.findBadgeImagesByNickname(nickname)
//                .stream()
//                .map(dto -> new BadgeImageInfoDTO.BadgeImageInfoDTO(buildBadgeUrl(dto.getBadgeImageS3SavedURL()), dto.getBadgeYearMonth()))
//                .collect(Collectors.toList());

        // S3 환경에서 이용할 코드 주석
        return badgeImageRepository.findBadgeImagesByNickname(nickname).stream()
                .map(projectionDTO -> {

                    String badgeImageS3SavedPath = buildBadgeImageS3RequestKey(projectionDTO.s3SavedObjectName());

                    String signedUrl = cloudFrontSignedUrlService.generateSignedUrl(badgeImageS3SavedPath);

                    return convertBadgeImageInfoDTO(projectionDTO, signedUrl);
                })
                .toList();
    }

    /**
     * 뱃지 URL 빌드 메서드 : 로컬용
     *
     * @param badgeImageS3SavedURL DB에 저장 되어있는 뱃지 이미지 URL
     * @return 뱃지 이미지 URL
     **/
    private String buildBadgeUrl(String badgeImageS3SavedURL) {

        String filename = badgeImageS3SavedURL.substring(badgeImageS3SavedURL.lastIndexOf("/") + 1);

        return "http://localhost:8080" + FileStorageUtil.getLocalStoreDir(filename);
    }

    /**
     * 뱃지 URL 부여 메서드
     *
     * @param top3MemberIds Top3 유저 ID
     * @param lastMonth 지난 달(년도 포함)
     */
    @Transactional
    public void assignBadgesToTopMembersByIds(List<Long> top3MemberIds, YearMonth lastMonth) {

        // 뱃지 이미지 파일 이름 예: "prod/images/badges/202407_badge.png"
        String badgeImageS3SavedPath = buildBadgeImageS3SavedObjectName(lastMonth);

        // DB에 저장할 뱃지 발급 연월 정보 예: 2024.07
        String badgeYearMonth = convertBadgeYearMonthToString(lastMonth);

        // 로컬 환경용 코드
        // 임시로 로컬에 저장된 뱃지 이미지 경로로 만듦.
//        String badgeImageURL = FileStorageUtil.getLocalStoreDir(badgeImageName);
//
//        // TOP3에 뱃지 이미지 부여, 저장
//        top3MemberIds.stream()
//                .map(memberId -> memberRepository.findById(memberId)
//                        .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다.")))
//                .map(member -> BadgeImage.buildBadgeImage(badgeImageS3SavedPath, badgeYearMonth, member))
//                .forEach(badgeImageRepository::save);

        // S3 환경에서 이용할 코드 주석

        // TOP3에 뱃지 이미지 부여, 저장
        top3MemberIds.stream()
                .map(memberId -> memberRepository.findById(memberId)
                        .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다.")))
                .map(member -> BadgeImage.buildBadgeImage(badgeImageS3SavedPath, badgeYearMonth, member))
                .forEach(badgeImageRepository::save);
    }

    /**
     * 초기 멤버 뱃지 URL 부여 메서드
     *
     * @param lastMonth 지난 달(년도 포함)
     */
    @Transactional
    public void assignBadgesToInitTestMembers(YearMonth lastMonth) {

        List<Long> initMemberIds = Arrays.asList(2L, 3L, 4L);

        // 뱃지 이미지 파일 이름 예: "prod/images/badges/202407_badge.png"
        String badgeImageS3SavedPath = buildBadgeImageS3SavedObjectName(lastMonth);

        // DB에 저장할 뱃지 발급 연월 정보 예: 2024.07
        String badgeYearMonth = convertBadgeYearMonthToString(lastMonth);

        // 로컬 환경용 코드
        // 임시로 로컬에 저장된 뱃지 이미지 경로로 만듦.
//        String badgeImageURL = FileStorageUtil.getLocalStoreDir(badgeImageName);
//
//        // TOP3에 뱃지 이미지 부여, 저장
//        top3MemberIds.stream()
//                .map(memberId -> memberRepository.findById(memberId)
//                        .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다.")))
//                .map(member -> BadgeImage.buildBadgeImage(badgeImageS3SavedPath, badgeYearMonth, member))
//                .forEach(badgeImageRepository::save);

        // S3 환경에서 이용할 코드 주석

        // 초기 멤버에게 뱃지 이미지 부여, 저장
        initMemberIds.stream()
                .map(memberId -> memberRepository.findById(memberId)
                        .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다.")))
                .map(member -> BadgeImage.buildBadgeImage(badgeImageS3SavedPath, badgeYearMonth, member))
                .forEach(badgeImageRepository::save);
    }
}
