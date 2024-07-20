package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.BadgeImageResponse;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.repository.MemberRepository;
import sumcoda.boardbuddy.repository.badgeImage.BadgeImageRepository;

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
    public BadgeImageResponse.BadgeImageListDTO getBadges (String nickname) {
        if (nickname == null) {
            throw new MemberRetrievalException("내부 시스템 문제로 뱃지 조회 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
        }

        if (Boolean.FALSE.equals(memberRepository.existsByNickname(nickname))) {
            throw new MemberNotFoundException("해당 닉네임을 가진 사용자를 찾을 수 없습니다.");
        }

        List<BadgeImageResponse.BadgeImageUrlDTO> badgeImageUrlDTOs = badgeImageRepository.findBadgeImagesByNickname(nickname);

        //로컬 테스트 시 사용
        List<String> badgeUrls = badgeImageUrlDTOs.stream()
                .map(dto -> "http://localhost:8080" + dto.getLocalSavedFileURL())
                .collect(Collectors.toList());

        //AWS 테스트 시 사용
//        List<String> badgeUrls = badgeImageUrlDTOs.stream()
//                .map(BadgeImageResponse.BadgeImageUrlDTO::getAwsS3SavedFileURL)
//                .collect(Collectors.toList());

        return BadgeImageResponse.BadgeImageListDTO.builder()
                .badges(badgeUrls)
                .build();
    }
}
