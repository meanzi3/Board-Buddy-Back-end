package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.config.AppConfig;
import sumcoda.boardbuddy.dto.BadgeImageResponse;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.repository.MemberRepository;
import sumcoda.boardbuddy.repository.badgeImage.BadgeImageRepository;
import sumcoda.boardbuddy.util.FileStorageUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeImageService {

    private final BadgeImageRepository badgeImageRepository;

    private final MemberRepository memberRepository;
    private final AppConfig appConfig;

    /**
     * 뱃지 조회 요청 캐치
     *
     * @param nickname 사용자가 입력한 닉네임
     * @return 뱃지 이미지 URL 리스트
     **/
    @Transactional
    public BadgeImageResponse.BadgeImageListDTO getBadges (String nickname) {
        if (nickname == null) {
            throw new MemberRetrievalException("뱃지 조회 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
        }

        if (Boolean.FALSE.equals(memberRepository.existsByNickname(nickname))) {
            throw new MemberNotFoundException("해당 유저를 찾을 수 없습니다.");
        }

        List<BadgeImageResponse.BadgeImageUrlDTO> badgeImageUrlDTOs = badgeImageRepository.findBadgeImagesByNickname(nickname);

        List<String> badgeUrl = badgeImageUrlDTOs.stream()
                .map(dto -> buildBadgeUrl(dto.getAwsS3SavedFileURL()))
                .collect(Collectors.toList());

        return BadgeImageResponse.BadgeImageListDTO.builder()
                .badges(badgeUrl)
                .build();
    }

    /**
     * 뱃지 URL 빌드 메서드
     *
     * @param awsS3SavedFileURL
     * @return 뱃지 이미지 URL
     **/
    private String buildBadgeUrl(String awsS3SavedFileURL) {
        String baseUrl = appConfig.getBaseUrl();

        String enviroment = appConfig.getEnviroment();

        String filename = awsS3SavedFileURL.substring(awsS3SavedFileURL.lastIndexOf("/") + 1);

        return "local".equalsIgnoreCase(enviroment) ? baseUrl + FileStorageUtil.getLocalStoreDir(filename) : awsS3SavedFileURL;
    }
}
