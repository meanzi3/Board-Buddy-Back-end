package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.PublicDistrictResponse;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.exception.publicDistrict.NoSearchResultException;
import sumcoda.boardbuddy.exception.publicDistrict.SearchLengthException;
import sumcoda.boardbuddy.repository.publicDistrict.PublicDistrictRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicDistrictService {

    // 최소 검색어 길이
    private static final int MINIMUM_SEARCH_LENGTH = 2;

    private final PublicDistrictRepository publicDistrictRepository;

    /**
     * 위치 검색
     *
     * @param emd 읍면동 검색어
     * @return 검색된 위치 정보 리스트
     */
    public List<PublicDistrictResponse.InfoDTO> searchLocations(String emd, String username) {

        // 유저 검증
        if (username == null) {
            throw new MemberRetrievalException("위치 검색 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
        }

        // 검색어 길이 검증
        if (emd.length() < MINIMUM_SEARCH_LENGTH) {
            throw new SearchLengthException("검색어는 두 글자 이상이어야 합니다.");
        }

        // redis 를 사용해서 위치 검색
        // 추후 로직 적용

        // mariadb 를 사용해서 위치 검색(redis 장애 시 mariadb 에서 검색)
        List<PublicDistrictResponse.InfoDTO> infoDTOs = publicDistrictRepository.findInfoDTOsByEmd(emd);

        // 검색 결과가 없는 경우 예외 처리
        if (infoDTOs.isEmpty()) {
            throw new NoSearchResultException("검색 결과가 없습니다. 검색어를 다시 확인해주세요.");
        }

        // 검색된 위치 정보 리스트 반환
        return infoDTOs;
    }
}
