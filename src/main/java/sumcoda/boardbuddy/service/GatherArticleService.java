package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.*;
import sumcoda.boardbuddy.entity.GatherArticle;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.entity.MemberGatherArticle;
import sumcoda.boardbuddy.entity.ParticipationApplication;
import sumcoda.boardbuddy.enumerate.MemberGatherArticleRole;
import sumcoda.boardbuddy.enumerate.ParticipationApplicationStatus;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleNotFoundException;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleAccessDeniedException;

import sumcoda.boardbuddy.enumerate.GatherArticleStatus;
import sumcoda.boardbuddy.exception.gatherArticle.*;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.exception.memberGatherArticle.MemberGatherArticleRetrievalException;
import sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepository;
import sumcoda.boardbuddy.repository.member.MemberRepository;
import sumcoda.boardbuddy.repository.memberGatherArticle.MemberGatherArticleRepository;
import sumcoda.boardbuddy.repository.participationApplication.ParticipationApplicationRepository;
import sumcoda.boardbuddy.util.GatherArticleValidationUtil;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GatherArticleService {

    private final MemberRepository memberRepository;

    private final GatherArticleRepository gatherArticleRepository;

    private final MemberGatherArticleRepository memberGatherArticleRepository;

    /**
     * @apiNote 현재는 사용률 저조로 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가시 다시 활성화될 수 있음
     */
//    private final NearPublicDistrictRepository nearPublicDistrictRepository;

    /**
     * @apiNote 현재는 사용률 저조로 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가시 다시 활성화될 수 있음
     */
//    private final PublicDistrictRepository publicDistrictRepository;

    private final ParticipationApplicationRepository participationApplicationRepository;

    private final GatherArticleStatusUpdateSchedulingService gatherArticleStatusUpdateSchedulingService;

    /**
     * @apiNote 현재는 사용률 저조로 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가시 다시 활성화될 수 있음
     */
//    private final PublicDistrictRedisService publicDistrictRedisService;

    private static final int PAGE_SIZE = 15;

    private static final int GATHER_ARTICLE_MINIMUM_SEARCH_LENGTH = 2;

    /**
     * 모집글 작성
     * @param createRequest
     * @param username
     * @return
     */
    @Transactional
    public GatherArticleResponse.CreateDTO createGatherArticle(GatherArticleRequest.CreateDTO createRequest, String username){

        // 사용자 검증
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("유효하지 않은 사용자입니다."));

        // 작성 요청 예외 검증, 처리
        GatherArticleValidationUtil.validateCreateRequest(createRequest);

        // 엔티티로 변환
        GatherArticle gatherArticle = createRequest.toEntity();

        // 저장
        gatherArticleRepository.save(gatherArticle);

        // memberGatherArticle 생성
        MemberGatherArticle memberGatherArticle = MemberGatherArticle.buildMemberGatherArticle(
                LocalDateTime.now(),
                MemberGatherArticleRole.AUTHOR,
                0,
                member,
                gatherArticle
        );

        // 저장
        memberGatherArticleRepository.save(memberGatherArticle);

        // participationApplication 생성
        ParticipationApplication participationApplication = ParticipationApplication.buildParticipationApplication(0, ParticipationApplicationStatus.APPROVED, memberGatherArticle);

        // 저장
        participationApplicationRepository.save(participationApplication);

        // 모임 완료 상태 업데이트 작업 스케줄링
        gatherArticleStatusUpdateSchedulingService.scheduleStatusUpdateJob(gatherArticle.getId(), gatherArticle.getEndDateTime());

        return GatherArticleResponse.CreateDTO.builder().id(gatherArticle.getId()).build();
    }

    /**
     * 모집글 조회
     * @param gatherArticleId, username
     * @return
     */
    public GatherArticleResponse.ReadDTO getGatherArticle(Long gatherArticleId, String username) {

        // 존재하는 모집글인지 확인
        GatherArticleResponse.IdDTO gatherArticleIdDTO = gatherArticleRepository.findIdDTOById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleNotFoundException("존재하지 않는 모집글입니다."));

        // 사용자 검증
        MemberResponse.IdDTO memberIdDTO = memberRepository.findIdDTOByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("유효하지 않은 사용자입니다."));

        return gatherArticleRepository.findGatherArticleReadDTOByGatherArticleId(gatherArticleIdDTO.getId(), memberIdDTO.getId());
    }

    /**
     * 모집글 수정
     * @param gatherArticleId
     * @param updateRequest
     * @param username
     * @return
     */
    @Transactional
    public GatherArticleResponse.UpdateDTO updateGatherArticle(Long gatherArticleId, GatherArticleRequest.UpdateDTO updateRequest, String username) {

        // 존재하는 모집글인지 확인
        GatherArticle gatherArticle = gatherArticleRepository.findById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleNotFoundException("존재하지 않는 모집글입니다."));

        // 사용자 검증
        MemberResponse.IdDTO memberIdDTO = memberRepository.findIdDTOByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("유효하지 않은 사용자입니다."));

        // 작성자 검증
        if (!memberGatherArticleRepository.isAuthor(gatherArticleId, memberIdDTO.getId())) {
            throw new GatherArticleAccessDeniedException("작성자만 수정할 수 있습니다.");
        }

        // 수정 요청 예외 검증, 처리
        GatherArticleValidationUtil.validateUpdateRequest(updateRequest, gatherArticle.getCurrentParticipants());

        // 기존 endDateTime
        LocalDateTime originalEndDateTime = gatherArticle.getEndDateTime();

        // 수정
        gatherArticle.update(updateRequest.getTitle(),
                updateRequest.getDescription(),
                updateRequest.getMeetingLocation(),
                updateRequest.getSido(),
                updateRequest.getSgg(),
                updateRequest.getEmd(),
                updateRequest.getX(),
                updateRequest.getY(),
                updateRequest.getMaxParticipants(),
                updateRequest.getStartDateTime(),
                updateRequest.getEndDateTime());

        // 모집글 상태 확인, 업데이트
        updateGatherArticleStatusBasedOnParticipants(gatherArticle);

        // endDateTime 이 바뀌었을 때만 리스케줄링 수행
        if (!originalEndDateTime.equals(updateRequest.getEndDateTime())) {
            gatherArticleStatusUpdateSchedulingService.rescheduleStatusUpdateJob(gatherArticle.getId(), gatherArticle.getEndDateTime());
        }

        return GatherArticleResponse.UpdateDTO.builder().id(gatherArticle.getId()).build();
    }

    /**
     * 모집글 삭제
     * @param gatherArticleId
     * @param username
     * @return
     */
    @Transactional
    public GatherArticleResponse.DeleteDTO deleteGatherArticle(Long gatherArticleId, String username) {

        // 존재하는 모집글인지 확인
        GatherArticleResponse.IdDTO GatherArticleIdDTO = gatherArticleRepository.findIdDTOById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleNotFoundException("존재하지 않는 모집글입니다."));

        // 사용자 검증
        MemberResponse.IdDTO memberIdDTO = memberRepository.findIdDTOByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("유효하지 않은 사용자입니다."));

        // 작성자인지 검증
        if (!memberGatherArticleRepository.isAuthor(GatherArticleIdDTO.getId(), memberIdDTO.getId())) {
            throw new GatherArticleAccessDeniedException("작성자만 삭제할 수 있습니다.");
        }

        // 삭제
        gatherArticleRepository.deleteById(GatherArticleIdDTO.getId());

        // 스케줄링 작업 취소
        gatherArticleStatusUpdateSchedulingService.unscheduleStatusUpdateJob(GatherArticleIdDTO.getId());

        return GatherArticleResponse.DeleteDTO.builder().id(GatherArticleIdDTO.getId()).build();
    }

    // 수정된 maxParticipants 에 따라 모집글 상태 변경
    @Transactional
    public void updateGatherArticleStatusBasedOnParticipants(GatherArticle gatherArticle) {
        GatherArticleStatus currentStatus = gatherArticle.getGatherArticleStatus();
        GatherArticleStatus newStatus;

        if (gatherArticle.getCurrentParticipants() >= gatherArticle.getMaxParticipants()) {
            newStatus = GatherArticleStatus.CLOSED;
        } else {
            newStatus = GatherArticleStatus.OPEN;
        }

        // 상태가 다를 때만 수정
        if (currentStatus != newStatus) {
            gatherArticle.assignGatherArticleStatus(newStatus);
        }
    }

    /**
     * 내가 작성한 모집글 조회
     *
     * @param username 회원의 username
     * @return 내가 작성한 모집글 DTO 리스트
     **/
    public List<GatherArticleResponse.GatherArticleInfosDTO> getMyGatherArticles(String username) {

        if (username == null) {
            throw new MemberRetrievalException("작성한 모집글 조회 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
        }

        return gatherArticleRepository.findGatherArticleInfosByUsername(username);
    }

    /**
     * 참가한 모집글 조회
     *
     * @param username 회원의 username
     * @return 참가한 모집글 DTO 리스트
     **/
    public List<GatherArticleResponse.MyParticipationInfosDTO> getMyParticipations(String username) {

        if (username == null) {
            throw new MemberRetrievalException("참가한 모집글 조회 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
        }

        return gatherArticleRepository.findParticipationsByUsername(username);
    }

    /**
     * @apiNote V1 - 내 동네 반경 n km 기반 모집글 리스트 조회 (현재 미사용, 향후 복원 가능)
     * 모집글 리스트 조회
     *
     * @param page     페이지 번호
     * @param status   모집 상태 (옵션)
     * @param sort     정렬 기준 (옵션)
     * @param username 사용자 이름
     * @return 모집글 리스트 DTO
     */
//    public GatherArticleResponse.ReadListDTO getGatherArticles(Integer page, String status, String sort, String username) {
//
//        // 정렬 기준 검증
//        if (sort != null && !sort.equals(GatherArticleStatus.SOON.getValue())) {
//            throw new GatherArticleSortException("유효하지 않은 정렬 기준입니다.");
//        }
//
//        // 모집글 상태 검증
//        if (status != null && !status.equals(GatherArticleStatus.OPEN.getValue())) {
//            throw new GatherArticleStatusException("유효하지 않은 모집글 상태입니다.");
//        }
//
//        // 사용자의 주변 행정 구역 얻어오기
//        List<NearPublicDistrictResponse.LocationDTO> locationDTOs = getLocationDTOsByUsername(username);
//
//        // 주변 행정 구역 리스트 생성
//        List<String> sidoList = new ArrayList<>();
//        List<String> sggList = new ArrayList<>();
//        List<String> emdList = new ArrayList<>();
//
//        // 주변 행정 구역 리스트에 데이터 추가
//        locationDTOs.forEach(district -> {
//            sidoList.add(district.getSido());
//            sggList.add(district.getSgg());
//            emdList.add(district.getEmd());
//        });
//
//        // 페이징 정보 생성
//        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
//        // AUTHOR 로 필터하기 위한 역할 선언
//        MemberGatherArticleRole role = MemberGatherArticleRole.AUTHOR;
//
//        // 모집글 리스트 조회
//        Slice<GatherArticleResponse.ReadSliceDTO> readSliceDTO = gatherArticleRepository.findReadSliceDTOByLocationAndStatusAndSort(
//                sidoList, sggList, emdList, status, sort, role, pageable);
//
//        // 모집글 리스트 DTO 생성 및 반환
//        return GatherArticleResponse.ReadListDTO.builder()
//                .posts(readSliceDTO.getContent())
//                .last(readSliceDTO.isLast())
//                .build();
//    }

    /**
     * @apiNote V2 - 사용자가 지정한 지역 기반 모집글 리스트 조회
     * 모집글 리스트 조회 (검색 포함)
     *
     * @param page      페이지 번호
     * @param sido      시도
     * @param sgg       시군구
     * @param status    모집 상태 (옵션)
     * @param sort      정렬 기준 (옵션)
     * @param keyword   검색어
     * @return          모집글 리스트 DTO
     */
    public GatherArticleResponse.ReadListDTO getGatherArticlesV2(
            Integer page,
            String sido,
            String sgg,
            String status,
            String sort,
            String keyword
    ) {

        // 정렬 기준 검증
        if (sort != null && !sort.equals(GatherArticleStatus.SOON.getValue())) {
            throw new GatherArticleSortException("유효하지 않은 정렬 기준입니다.");
        }

        // 모집글 상태 검증
        if (status != null && !status.equals(GatherArticleStatus.OPEN.getValue())) {
            throw new GatherArticleStatusException("유효하지 않은 모집글 상태입니다.");
        }

        // 검색어 검증
        if (keyword != null && keyword.length() < 2) {
            throw new GatherArticleSearchLengthException("검색어는 두 글자 이상이어야 합니다.");
        }

        // 페이징 정보 생성
        Pageable pageable = PageRequest.of(page, 10);
        // AUTHOR 로 필터하기 위한 역할 선언
        MemberGatherArticleRole role = MemberGatherArticleRole.AUTHOR;

        // 모집글 리스트 조회
        Slice<GatherArticleResponse.ReadSliceDTO> result = gatherArticleRepository.findReadSliceDTOByLocationV2AndStatusAndSortAndKeyword(
                sido, sgg, status, sort, keyword, role, pageable);

        // 모집글 리스트 DTO 생성 및 반환
        return GatherArticleResponse.ReadListDTO.builder()
                .posts(result.getContent())
                .last(result.isLast())
                .build();

    }

    /**
     * 채팅방 정보와 연관된 모집글 간단 정보 조회
     *
     * @param chatRoomId 채팅방 Id
     * @param gatherArticleId 모집글 Id
     * @param username 사용자 아이디
     * @return 채팅방과 연관된 모집글 간단 정보
     **/
    public GatherArticleResponse.SummaryInfoDTO getChatRoomGatherArticleSimpleInfo(Long chatRoomId, Long gatherArticleId, String username) {

        // 모집글 정보 조회
        boolean isGatherArticleExists = gatherArticleRepository.existsByChatRoomIdAndId(chatRoomId, gatherArticleId);

        if (!isGatherArticleExists) {
            throw new GatherArticleNotFoundException("모집글 정보를 찾을 수 없습니다.");
        }

        Boolean isMemberGatherArticleExists = memberGatherArticleRepository.existsByGatherArticleIdAndMemberUsername(gatherArticleId, username);

        if (!isMemberGatherArticleExists) {
            throw new MemberGatherArticleRetrievalException("서버 문제로 해당 모집글 관련 사용자의 정보를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        return gatherArticleRepository.findSimpleInfoByGatherArticleId(gatherArticleId)
                .orElseThrow(() -> new GatherArticleRetrievalException("서버 문제로 해당 모집글에 대한 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));
    }

    /**
     * @apiNote V1 - 내 동네 반경 n km 기반 모집글 검색 (현재 미사용, 향후 복원 가능)
     * 모집글 검색
     * @param keyword   검색어
     * @param username  사용자 username
     * @return          검색 결과 리스트
     */
//    public List<GatherArticleResponse.SearchResultDTO> searchArticles(String keyword, String username) {
//
//        // 검색어 길이 검증
//        if (keyword.length() < GATHER_ARTICLE_MINIMUM_SEARCH_LENGTH) {
//            throw new GatherArticleSearchLengthException("검색어는 두 글자 이상이어야 합니다.");
//        }
//
//        // 사용자의 주변 행정 구역 얻어오기
//        List<NearPublicDistrictResponse.LocationDTO> locationDTOs = getLocationDTOsByUsername(username);
//
//        // 주변 행정 구역 리스트 생성
//        List<String> sidoList = new ArrayList<>();
//        List<String> sggList = new ArrayList<>();
//        List<String> emdList = new ArrayList<>();
//
//        locationDTOs.forEach(district -> {
//            sidoList.add(district.getSido());
//            sggList.add(district.getSgg());
//            emdList.add(district.getEmd());
//        });
//
//        // AUTHOR 로 필터하기 위한 역할 선언
//        MemberGatherArticleRole role = MemberGatherArticleRole.AUTHOR;
//
//        // 검색어를 이용한 모집글 조회
//        List<GatherArticleResponse.SearchResultDTO> searchResultDTOs = gatherArticleRepository.findSearchResultDTOByKeyword(
//                sidoList, sggList, emdList, role, keyword);
//
//        if(searchResultDTOs.isEmpty()){
//            throw new GatherArticleNoSearchResultException("검색 결과가 없습니다.");
//        }
//
//        return searchResultDTOs;
//    }

    /**
     * @apiNote 임시 비활성화된 상태
     *          위치 관련 코드 제거 필요
     */
//    // 사용자의 위치와 설정한 반경을 기반으로 주변 행정 구역을 얻음
//    private List<NearPublicDistrictResponse.LocationDTO> getLocationDTOsByUsername(String username) {
//        // 사용자 위치 및 반경 정보 조회
//        MemberResponse.LocationWithRadiusDTO locationWithRadiusDTO = memberRepository.findLocationWithRadiusDTOByUsername(username)
//                .orElseThrow(() -> new MemberRetrievalException("해당 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));
//
//        // 사용자의 위치 선언
//        String sido = locationWithRadiusDTO.getSido();
//        String sgg = locationWithRadiusDTO.getSgg();
//        String emd = locationWithRadiusDTO.getEmd();
//
//
//        // redis 에서 조회 - 기준 위치에 해당하는 IdDTO 를 조회
//        PublicDistrictResponse.IdDTO idDTO = publicDistrictRedisService.findIdDTOBySidoAndSggAndEmd(sido, sgg, emd)
//                .orElseGet(() -> {
//                    // mariadb 에서 조회 - 기준 위치에 해당하는 IdDTO 를 조회(redis 장애 발생 시 mariadb 에서 조회)
//                    log.error("[redis findIdDTOBySidoAndSggAndEmd() error]");
//                    return publicDistrictRepository.findIdDTOBySidoAndSggAndEmd(sido, sgg, emd)
//                            .orElseThrow(() -> new PublicDistrictRetrievalException("유저의 위치 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));
//                });
//
//        // 사용자의 위치와 반경 정보로 주변 행정 구역 조회
//        List<NearPublicDistrictResponse.LocationDTO> locationDTOs = nearPublicDistrictRepository.findLocationDTOsByPublicDistrictIdAndRadius(
//                idDTO.getId(), locationWithRadiusDTO.getRadius());
//
//        // 주변 행정 구역이 없는 경우 예외 처리
//        if (locationDTOs.isEmpty()) {
//            throw new NearPublicDistrictRetrievalException("유저의 주변 행정 구역을 찾을 수 없습니다. 관리자에게 문의하세요.");
//        }
//
//        return locationDTOs;
//    }
}
