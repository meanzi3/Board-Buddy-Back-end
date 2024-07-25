package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.BadgeImageResponse;
import sumcoda.boardbuddy.entity.BadgeImage;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.repository.MemberRepository;
import sumcoda.boardbuddy.repository.badgeImage.BadgeImageRepository;
import sumcoda.boardbuddy.util.FileStorageUtil;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeImageService {

    private final BadgeImageRepository badgeImageRepository;

    private final MemberRepository memberRepository;

    private static final String BADGE_IMAGE_SUFFIX = "_badge.png";

    /**
     * 뱃지 조회 요청 캐치
     *
     * @param nickname 사용자가 입력한 닉네임
     * @return 뱃지 이미지 URL 리스트
     **/
    public List<BadgeImageResponse.BadgeImageUrlDTO> getBadges (String nickname) {
        if (nickname == null) {
            throw new MemberRetrievalException("뱃지 조회 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
        }

        if (Boolean.FALSE.equals(memberRepository.existsByNickname(nickname))) {
            throw new MemberNotFoundException("해당 유저를 찾을 수 없습니다.");
        }

        return badgeImageRepository.findBadgeImagesByNickname(nickname)
                .stream()
                .map(dto -> new BadgeImageResponse.BadgeImageUrlDTO(buildBadgeUrl(dto.getBadgeImageS3SavedURL())))
                .collect(Collectors.toList());
    }

    /**
     * 뱃지 URL 빌드 메서드
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

        // 임시로 로컬에 저장된 뱃지 이미지 경로로 만듦.
        String badgeImageURL = FileStorageUtil.getLocalStoreDir(badgeImageName);

        // TODO: S3 이용 시 url 빌드 추가 코드 필요

        // TOP3에 뱃지 이미지 부여, 저장
        top3MemberIds.stream()
                .map(memberId -> memberRepository.findById(memberId)
                        .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다.")))
                .map(member -> BadgeImage.buildBadgeImage(badgeImageName, badgeImageURL, member))
                .forEach(badgeImageRepository::save);
    }
}
