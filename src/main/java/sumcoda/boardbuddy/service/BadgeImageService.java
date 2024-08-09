package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.config.AwsS3Config;
import sumcoda.boardbuddy.dto.BadgeImageResponse;
import sumcoda.boardbuddy.entity.BadgeImage;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.repository.member.MemberRepository;
import sumcoda.boardbuddy.repository.badgeImage.BadgeImageRepository;
import sumcoda.boardbuddy.util.FileStorageUtil;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeImageService {

    private final BadgeImageRepository badgeImageRepository;

    private final MemberRepository memberRepository;

    private static final String BADGE_IMAGE_SUFFIX = "_badge.png";

    // AWS S3 활용을 위해 필요한 설정 클래스
    private final AwsS3Config awsS3Config;

    // S3에 등록된 버킷 이름
    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String bucketName;

    /**
     * 뱃지 조회 요청 캐치
     *
     * @param nickname 사용자가 입력한 닉네임
     * @return 뱃지 이미지 URL 리스트
     **/
    public List<BadgeImageResponse.BadgeImageInfosDTO> getBadges (String nickname) {
        if (nickname == null) {
            throw new MemberRetrievalException("뱃지 조회 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
        }

        if (Boolean.FALSE.equals(memberRepository.existsByNickname(nickname))) {
            throw new MemberNotFoundException("해당 유저를 찾을 수 없습니다.");
        }

        // 로컬 환경용 코드
//        return badgeImageRepository.findBadgeImagesByNickname(nickname)
//                .stream()
//                .map(dto -> new BadgeImageResponse.BadgeImageInfosDTO(buildBadgeUrl(dto.getBadgeImageS3SavedURL()), dto.getBadgeYearMonth()))
//                .collect(Collectors.toList());

        // S3 환경에서 이용할 코드 주석
        return badgeImageRepository.findBadgeImagesByNickname(nickname);
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
     * @param top3MemberIds
     * @param lastMonth
     */
    @Transactional
    public void assignBadgesToTopMembersByIds(List<Long> top3MemberIds, YearMonth lastMonth) {
        // 받을 뱃지의 년, 월 문자열 구하기 예: "202407"
        String lastMonthStr = lastMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));

        // 뱃지 이미지 파일 이름 예: "202407_badge.png"
        String badgeImageName = lastMonthStr + BADGE_IMAGE_SUFFIX;

        // DB에 저장할 뱃지 발급 연월 정보 예: 2024.07
        String badgeYearMonth = lastMonth.format(DateTimeFormatter.ofPattern("yyyy.MM"));

        // 로컬 환경용 코드
        // 임시로 로컬에 저장된 뱃지 이미지 경로로 만듦.
//        String badgeImageURL = FileStorageUtil.getLocalStoreDir(badgeImageName);
//
//        // TOP3에 뱃지 이미지 부여, 저장
//        top3MemberIds.stream()
//                .map(memberId -> memberRepository.findById(memberId)
//                        .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다.")))
//                .map(member -> BadgeImage.buildBadgeImage(badgeImageName, badgeImageURL, badgeYearMonth, member))
//                .forEach(badgeImageRepository::save);

        // S3 환경에서 이용할 코드 주석
        // 클라이언트가 해당 이미지를 요청할 수 있는 URL
        String awsS3URL = awsS3Config.amazonS3Client().getUrl(bucketName, badgeImageName).toString();

        // TOP3에 뱃지 이미지 부여, 저장
        top3MemberIds.stream()
                .map(memberId -> memberRepository.findById(memberId)
                        .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다.")))
                .map(member -> BadgeImage.buildBadgeImage(badgeImageName, awsS3URL, badgeYearMonth, member))
                .forEach(badgeImageRepository::save);
    }

    /**
     * 초기 멤버 뱃지 URL 부여 메서드
     *
     * @param top3MemberIds
     * @param lastMonth
     */
    @Transactional
    public void assignBadgesToInitTestMembers(YearMonth lastMonth) {

        List<Long> initMemberIds = Arrays.asList(2L, 3L, 4L);

        // 받을 뱃지의 년, 월 문자열 구하기 예: "202407"
        String lastMonthStr = lastMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));

        // 뱃지 이미지 파일 이름 예: "202407_badge.png"
        String badgeImageName = lastMonthStr + BADGE_IMAGE_SUFFIX;

        // DB에 저장할 뱃지 발급 연월 정보 예: 2024.07
        String badgeYearMonth = lastMonth.format(DateTimeFormatter.ofPattern("yyyy.MM"));

        // 로컬 환경용 코드
        // 임시로 로컬에 저장된 뱃지 이미지 경로로 만듦.
//        String badgeImageURL = FileStorageUtil.getLocalStoreDir(badgeImageName);
//
//        // TOP3에 뱃지 이미지 부여, 저장
//        top3MemberIds.stream()
//                .map(memberId -> memberRepository.findById(memberId)
//                        .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다.")))
//                .map(member -> BadgeImage.buildBadgeImage(badgeImageName, badgeImageURL, badgeYearMonth, member))
//                .forEach(badgeImageRepository::save);

        // S3 환경에서 이용할 코드 주석
        // 클라이언트가 해당 이미지를 요청할 수 있는 URL
        String awsS3URL = aws3Config.amazonS3Client().getUrl(bucketName, badgeImageName).toString();

        // 초기 멤버에게 뱃지 이미지 부여, 저장
        initMemberIds.stream()
                .map(memberId -> memberRepository.findById(memberId)
                        .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다.")))
                .map(member -> BadgeImage.buildBadgeImage(badgeImageName, awsS3URL, badgeYearMonth, member))
                .forEach(badgeImageRepository::save);


    }
}
