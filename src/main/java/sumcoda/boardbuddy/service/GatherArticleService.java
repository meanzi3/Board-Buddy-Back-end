package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
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
import sumcoda.boardbuddy.exception.nearPublicDistrict.NearPublicDistrictRetrievalException;
import sumcoda.boardbuddy.exception.publicDistrict.PublicDistrictRetrievalException;
import sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepository;
import sumcoda.boardbuddy.repository.MemberRepository;
import sumcoda.boardbuddy.repository.memberGatherArticle.MemberGatherArticleRepository;
import sumcoda.boardbuddy.repository.nearPublicDistric.NearPublicDistrictRepository;
import sumcoda.boardbuddy.repository.participationApplication.ParticipationApplicationRepository;
import sumcoda.boardbuddy.repository.publicDistrict.PublicDistrictRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GatherArticleService {

    private final MemberRepository memberRepository;

    private final GatherArticleRepository gatherArticleRepository;

    private final MemberGatherArticleRepository memberGatherArticleRepository;

    private final NearPublicDistrictRepository nearPublicDistrictRepository;

    private final PublicDistrictRepository publicDistrictRepository;

    private static final int PAGE_SIZE = 15;
    private final ParticipationApplicationRepository participationApplicationRepository;

    private final GatherArticleStatusUpdateSchedulingService gatherArticleStatusUpdateSchedulingService;

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
        validateCreateRequest(createRequest);

        // 엔티티로 변환
        GatherArticle gatherArticle = createRequest.toEntity();

        // 저장
        gatherArticleRepository.save(gatherArticle);

        // memberGatherArticle 생성
        MemberGatherArticle memberGatherArticle = MemberGatherArticle.buildMemberGatherArticle(
                LocalDateTime.now(),
                MemberGatherArticleRole.AUTHOR,
                member,
                gatherArticle
        );

        // 저장
        memberGatherArticleRepository.save(memberGatherArticle);

        // participationApplication 생성
        ParticipationApplication participationApplication = ParticipationApplication.buildParticipationRequest(0, ParticipationApplicationStatus.APPROVED, memberGatherArticle);

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

        // 예외 검증, 처리
        validateUpdateRequest(updateRequest, gatherArticle.getCurrentParticipants());

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

    /**
     * 작성 요청 검증
     * @param createRequest
     */
    private void validateCreateRequest(GatherArticleRequest.CreateDTO createRequest) {
        if (createRequest.getTitle() == null || createRequest.getTitle().isEmpty()) {
            throw new GatherArticleSaveException("제목이 입력되지 않았습니다.");
        }
        if (createRequest.getDescription() == null || createRequest.getDescription().isEmpty()) {
            throw new GatherArticleSaveException("설명이 입력되지 않았습니다.");
        }
        if (createRequest.getMeetingLocation() == null || createRequest.getMeetingLocation().isEmpty()) {
            throw new GatherArticleSaveException("장소가 입력되지 않았습니다.");
        }
        if (createRequest.getSido() == null || createRequest.getSido().isEmpty()) {
            throw new GatherArticleSaveException("시, 도가 입력되지 않았습니다.");
        }
        if (createRequest.getSgg() == null || createRequest.getSgg().isEmpty()) {
            throw new GatherArticleSaveException("시, 군, 구가 입력되지 않았습니다.");
        }
        if (createRequest.getEmd() == null || createRequest.getEmd().isEmpty()) {
            throw new GatherArticleSaveException("읍, 면, 동이 입력되지 않았습니다.");
        }
        if (createRequest.getX() == null) {
            throw new GatherArticleSaveException("경도가 입력되지 않았습니다.");
        }
        if (createRequest.getY() == null) {
            throw new GatherArticleSaveException("위도가 입력되지 않았습니다.");
        }
        if (createRequest.getStartDateTime() == null) {
            throw new GatherArticleSaveException("시작 시간이 입력되지 않았습니다.");
        }
        if (createRequest.getEndDateTime() == null) {
            throw new GatherArticleSaveException("종료 시간이 입력되지 않았습니다.");
        }
        if (createRequest.getMaxParticipants() == null || createRequest.getMaxParticipants() <= 0) {
            throw new GatherArticleSaveException("최대 참가 인원이 유효하지 않습니다.");
        }
    }

    /** 수정 요청 검증
     * @param updateRequest
     * @param currentParticipants
     */
    private void validateUpdateRequest(GatherArticleRequest.UpdateDTO updateRequest, int currentParticipants) {
        if (updateRequest.getTitle() == null || updateRequest.getTitle().isEmpty()) {
            throw new GatherArticleUpdateException("제목이 입력되지 않았습니다.");
        }
        if (updateRequest.getDescription() == null || updateRequest.getDescription().isEmpty()) {
            throw new GatherArticleUpdateException("설명이 입력되지 않았습니다.");
        }
        if (updateRequest.getMeetingLocation() == null || updateRequest.getMeetingLocation().isEmpty()) {
            throw new GatherArticleUpdateException("장소가 입력되지 않았습니다.");
        }
        if (updateRequest.getSido() == null || updateRequest.getSido().isEmpty()) {
            throw new GatherArticleUpdateException("시, 도가 입력되지 않았습니다.");
        }
        if (updateRequest.getSgg() == null || updateRequest.getSgg().isEmpty()) {
            throw new GatherArticleUpdateException("시, 군, 구가 입력되지 않았습니다.");
        }
        if (updateRequest.getEmd() == null || updateRequest.getEmd().isEmpty()) {
            throw new GatherArticleUpdateException("읍, 면, 동이 입력되지 않았습니다.");
        }
        if (updateRequest.getX() == null) {
            throw new GatherArticleUpdateException("경도가 입력되지 않았습니다.");
        }
        if (updateRequest.getY() == null) {
            throw new GatherArticleUpdateException("위도가 입력되지 않았습니다.");
        }
        if (updateRequest.getStartDateTime() == null) {
            throw new GatherArticleUpdateException("시작 시간이 입력되지 않았습니다.");
        }
        if (updateRequest.getEndDateTime() == null) {
            throw new GatherArticleUpdateException("종료 시간이 입력되지 않았습니다.");
        }
        if (updateRequest.getMaxParticipants() == null || updateRequest.getMaxParticipants() <= 0) {
            throw new GatherArticleUpdateException("최대 참가 인원이 유효하지 않습니다.");
        }
        if (updateRequest.getMaxParticipants() < currentParticipants) {
            throw new GatherArticleUpdateException("최대 참가 인원은 현재 참가 인원보다 적을 수 없습니다.\n 현재 참가 인원 : "
                    + currentParticipants + "명");
        }
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
    public List<GatherArticleResponse.GatherArticleInfosDTO> getMyParticipations(String username) {

        if (username == null) {
            throw new MemberRetrievalException("참가한 모집글 조회 요청을 처리할 수 없습니다. 관리자에게 문의하세요.");
        }

        return gatherArticleRepository.findParticipationsByUsername(username);
    }

    /**
     * 모집글 리스트 조회
     *
     * @param page     페이지 번호
     * @param status   모집 상태 (옵션)
     * @param sort     정렬 기준 (옵션)
     * @param username 사용자 이름
     * @return 모집글 리스트 DTO
     */
    public GatherArticleResponse.ReadListDTO getGatherArticles(Integer page, String status, String sort, String username) {

        // 정렬 기준 검증
        if (sort != null && !sort.equals(GatherArticleStatus.SOON.getValue())) {
            throw new GatherArticleSortException("유효하지 않은 정렬 기준입니다.");
        }

        // 모집글 상태 검증
        if (status != null && !status.equals(GatherArticleStatus.OPEN.getValue())) {
            throw new GatherArticleStatusException("유효하지 않은 모집글 상태입니다.");
        }

        // 사용자 위치 및 반경 정보 조회
        MemberResponse.LocationWithRadiusDTO locationWithRadiusDTO = memberRepository.findLocationWithRadiusDTOByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("해당 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        // 기준 위치에 해당하는 행정 구역을 조회
        PublicDistrictResponse.LocationWithIdDTO locationWithIdDTO = publicDistrictRepository.findLocationWithIdDTOBySidoAndSggAndEmd(
                        locationWithRadiusDTO.getSido(), locationWithRadiusDTO.getSgg(), locationWithRadiusDTO.getEmd())
                .orElseThrow(() -> new PublicDistrictRetrievalException("유저의 위치 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));

        // 사용자의 위치와 반경 정보로 주변 행정 구역 조회
        List<NearPublicDistrictResponse.LocationDTO> locationDTOs = nearPublicDistrictRepository.findLocationDTOsByPublicDistrictIdAndRadius(
                locationWithIdDTO.getId(), locationWithRadiusDTO.getRadius());

        // 주변 행정 구역이 없는 경우 예외 처리
        if (locationDTOs.isEmpty()) {
            throw new NearPublicDistrictRetrievalException("유저의 주변 행정 구역을 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        // 주변 행정 구역 리스트 생성
        List<String> sidoList = new ArrayList<>();
        List<String> sggList = new ArrayList<>();
        List<String> emdList = new ArrayList<>();

        // 주변 행정 구역 리스트에 데이터 추가
        locationDTOs.forEach(district -> {
            sidoList.add(district.getSido());
            sggList.add(district.getSgg());
            emdList.add(district.getEmd());
        });

        // 페이징 정보 생성
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        // 모집글 리스트 조회
        Slice<GatherArticleResponse.ReadSliceDTO> readSliceDTO = gatherArticleRepository.findReadSliceDTOByLocationAndStatusAndSort(
                sidoList, sggList, emdList, status, sort, pageable);

        // 모집글 리스트 DTO 생성 및 반환
        return GatherArticleResponse.ReadListDTO.builder()
                .posts(readSliceDTO.getContent())
                .last(readSliceDTO.isLast())
                .build();
    }
}
