package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * 뱃지 조회 요청 캐치
     *
     * @param nickname 사용자가 입력한 닉네임
     * @return 뱃지 이미지 URL 리스트
     **/
    @Transactional
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
}
