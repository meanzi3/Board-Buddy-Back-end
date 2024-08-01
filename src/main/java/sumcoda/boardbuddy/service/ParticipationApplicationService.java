package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.dto.ParticipationApplicationResponse;
import sumcoda.boardbuddy.entity.GatherArticle;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.entity.MemberGatherArticle;
import sumcoda.boardbuddy.entity.ParticipationApplication;
import sumcoda.boardbuddy.enumerate.GatherArticleStatus;
import sumcoda.boardbuddy.enumerate.MemberGatherArticleRole;
import sumcoda.boardbuddy.enumerate.ParticipationApplicationStatus;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleNotFoundException;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleRetrievalException;
import sumcoda.boardbuddy.exception.gatherArticle.NotAuthorOfGatherArticleException;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.exception.memberGatherArticle.*;
import sumcoda.boardbuddy.exception.participationApplication.*;
import sumcoda.boardbuddy.repository.MemberRepository;
import sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepository;
import sumcoda.boardbuddy.repository.memberGatherArticle.MemberGatherArticleRepository;
import sumcoda.boardbuddy.repository.participationApplication.ParticipationApplicationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationApplicationService {

    private final MemberRepository memberRepository;

    private final GatherArticleRepository gatherArticleRepository;

    private final MemberGatherArticleRepository memberGatherArticleRepository;

    private final ParticipationApplicationRepository participationApplicationRepository;

    /**
     * 모집글 참가 신청 처리
     *
     * @param gatherArticleId 모집글 Id
     * @param username 참가신청하는 보내는 사용자 아이디
     **/
    @Transactional
    public void applyParticipation(Long gatherArticleId, String username) {

        GatherArticle gatherArticle = gatherArticleRepository.findById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleNotFoundException("해당 모집글이 존재하지 않습니다."));

        Boolean isMemberGatherArticleExists = memberGatherArticleRepository.existsByGatherArticleIdAndMemberUsername(gatherArticleId, username);

        Boolean isParticipationApplicationExists = participationApplicationRepository.existsByGatherArticleInAndUsername(gatherArticleId, username);
        // 이미 해당 모집글에 참가신청한 이력이 있는지 확인
        if (isMemberGatherArticleExists && isParticipationApplicationExists) {
            ParticipationApplication participationApplication = participationApplicationRepository.findByGatherArticleIdAndMemberUsername(gatherArticleId, username)
                    .orElseThrow(() -> new ParticipationApplicationRetrievalException("서버 문제로 참가 신청 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));

            // 해당 모집글에 이미 참가 확정이 되었는지 확인
            if (participationApplication.getParticipationApplicationStatus() == ParticipationApplicationStatus.APPROVED){
                throw new AlreadyApprovedParticipantException("이미 참가 승인된 모집글입니다.");
            }

            // 동일한 모집글의 참가신청 거절 이력이 3번을 초과하는지 확인
            if (participationApplication.getParticipationApplicationStatus() == ParticipationApplicationStatus.REJECTED) {
                throw new MaxRejectionsExceededException("동일한 모집글에 대해서 3번이상 거절 당했으므로, 더 이상 참가 신청할 수 없는 모집글입니다.");
            }

            // 본인이 참가 취소 신청을 했는지 확인
            if (participationApplication.getParticipationApplicationStatus() == ParticipationApplicationStatus.CANCELED) {
                throw new CannotReApplyAfterCancellationException("참가 신청자 본인의 참가취소로 인해 동일한 모집글에 다시 참가 신청할 수 없는 모집글입니다.");
            }

            // 참가 인원 정원을 초과한 참가 신청인지 확인
            if (gatherArticle.getCurrentParticipants() >= gatherArticle.getMaxParticipants()) {
                throw new ParticipationApplicationLimitExceededException("해당 모집글의 참가 인원이 이미 가득 찼습니다.");
            }

            participationApplication.assignParticipationApplicationStatus(ParticipationApplicationStatus.PENDING);

            return;
        }

        // 참가 인원 정원을 초과한 참가 신청인지 확인
        if (gatherArticle.getCurrentParticipants() >= gatherArticle.getMaxParticipants()) {
            throw new ParticipationApplicationLimitExceededException("해당 모집글의 참가 인원이 이미 가득 찼습니다.");
        }

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("서버 문제로 해당 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        MemberGatherArticle memberGatherArticle = MemberGatherArticle.buildMemberGatherArticle(
                null, MemberGatherArticleRole.NONE, 0, member, gatherArticle);

        Long memberGatherArticleId = memberGatherArticleRepository.save(memberGatherArticle).getId();

        if (memberGatherArticleId == null) {
            throw new MemberGatherArticleSaveException("서버 문제로 해당 모집글 관련한 사용자의 정보 저장을 실패하였습니다. 관리자에게 문의하세요.");
        }

        ParticipationApplication participationApplication = ParticipationApplication.builder()
                .rejectedParticipationCount(0)
                .participationApplicationStatus(ParticipationApplicationStatus.PENDING)
                .memberGatherArticle(memberGatherArticle)
                .build();

        Long participationApplicationId = participationApplicationRepository.save(participationApplication).getId();

        if (participationApplicationId == null) {
            throw new ParticipationApplicationSaveException("서버 문제로 해당 참가신청 정보 저장을 실패하였습니다. 관리자에게 문의하세요.");
        }
    }

    /**
     * 모집글 작성자가 회원의 참가 신청을 승인 처리
     *
     * @param gatherArticleId 모집글 Id
     * @param participationApplicationId 참가 신청 Id
     * @param username 참가신청을 승인하는 모집글 작성자 아이디
     **/
    @Transactional
    public String approveParticipationApplication(Long gatherArticleId, Long participationApplicationId, String username, String applicantNickname) {

        GatherArticle gatherArticle = gatherArticleRepository.findById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleRetrievalException("서버 문제로 해당 모집글 정보를 찾을 수 없습니다. 관리자에게 문의하세요"));

        Boolean isMemberAuthorOfGatherArticle = gatherArticleRepository.isMemberAuthorOfGatherArticle(gatherArticleId, username);

        if (!isMemberAuthorOfGatherArticle) {
            throw new NotAuthorOfGatherArticleException("해당 모집글의 작성자가 아니므로 참가신청 수락 권한이 없습니다.");
        }

        ParticipationApplication participationApplication = participationApplicationRepository.findById(participationApplicationId)
                .orElseThrow(() -> new ParticipationApplicationNotFoundException("유효하지 않은 참가 신청입니다."));

        // 해당 모집글에 이미 참가 완료 되었는지 확인
        if (participationApplication.getParticipationApplicationStatus() == ParticipationApplicationStatus.APPROVED) {
            throw new AlreadyApprovedParticipantException("이미 참가 승인된 사용자입니다.");
        }

        // 동일한 모집글의 참가신청 거절 이력이 3번을 초과하는지 확인
        if (participationApplication.getParticipationApplicationStatus() == ParticipationApplicationStatus.REJECTED) {
            throw new MaxRejectionsExceededException("동일한 모집글에 대해서 3번이상 거절 당했으므로, 더 이상 참가 신청할 수 없는 사용자입니다.");
        }

        // 본인이 참가 취소 신청을 했는지 확인
        if (participationApplication.getParticipationApplicationStatus() == ParticipationApplicationStatus.CANCELED) {
            throw new CannotReApplyAfterCancellationException("참가 신청자 본인의 참가 취소로 인해 동일한 모집글에 다시 참여 신청할 수 없는 사용자입니다.");
        }

        // 참가 인원 정원을 초과한 참가 신청인지 확인
        if (gatherArticle.getCurrentParticipants() >= gatherArticle.getMaxParticipants()) {
            throw new ParticipationApplicationLimitExceededException("해당 모집글의 참가 인원이 이미 가득 찼습니다.");
        }

        MemberGatherArticle memberGatherArticle = memberGatherArticleRepository.findByParticipationApplicationId(participationApplicationId)
                .orElseThrow(() -> new MemberGatherArticleRetrievalException("서버 문제로 해당 모집글 관련한 사용자의 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));


        memberGatherArticle.assignMemberGatherArticleRole(MemberGatherArticleRole.PARTICIPANT);

        memberGatherArticle.assignJoinedAt(LocalDateTime.now());

        participationApplication.assignParticipationApplicationStatus(ParticipationApplicationStatus.APPROVED);

        gatherArticle.assignCurrentParticipants(gatherArticle.getCurrentParticipants() + 1);

        // 모집글 상태 확인, 업데이트
        updateGatherArticleStatusBasedOnParticipants(gatherArticle);

        MemberResponse.UserNameDTO userNameDTO = memberRepository.findUsernameDTOByNickname(applicantNickname).orElseThrow(() -> new MemberNotFoundException("참가 승인할 사용자의 정보를 찾을 수 없습니다."));

        String applicantUsername = userNameDTO.getUsername();
        if (applicantUsername == null) {
            throw new MemberRetrievalException("서버 문제로 참가 승인할 사용자의 정보를 찾을 수 없습니다.");
        }

        return applicantUsername;
    }

    /**
     * 모집글 작성자가 회원의 참가 신청을 거절 처리
     *
     * @param gatherArticleId 모집글 Id
     * @param participationApplicationId 참가 신청 Id
     * @param username 참가신청을 거절하는 모집글 작성자 아이디
     **/
    @Transactional
    public void rejectParticipationApplication(Long gatherArticleId, Long participationApplicationId, String username) {
        boolean isGatherArticleExists = gatherArticleRepository.existsById(gatherArticleId);
        if (!isGatherArticleExists) {
            throw new GatherArticleRetrievalException("서버 문제로 해당 모집글을 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        Boolean isMemberAuthorOfGatherArticle = gatherArticleRepository.isMemberAuthorOfGatherArticle(gatherArticleId, username);

        if (!isMemberAuthorOfGatherArticle) {
            throw new NotAuthorOfGatherArticleException("해당 모집글의 작성자가 아니므로 참가신청 거절 권한이 없습니다.");
        }

        Boolean isMemberGatherArticleExists = memberGatherArticleRepository.existsByParticipationApplicationId(participationApplicationId);

        if (!isMemberGatherArticleExists) {
            throw new MemberGatherArticleRetrievalException("서버 문제로 해당 모집글 관련한 사용자의 정보를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        ParticipationApplication participationApplication = participationApplicationRepository.findById(participationApplicationId)
                .orElseThrow(() -> new ParticipationApplicationNotFoundException("유효하지 않은 참가 신청입니다."));

        // 해당 모집글에 이미 참가 확정이 되었는지 확인
        if (participationApplication.getParticipationApplicationStatus() == ParticipationApplicationStatus.APPROVED) {
            throw new AlreadyApprovedParticipantException("이미 참가 승인된 사용자 입니다.");
        }

        // 동일한 모집글의 참가신청 거절 이력이 3번을 초과하는지 확인
        if (participationApplication.getParticipationApplicationStatus() == ParticipationApplicationStatus.REJECTED) {
            throw new MaxRejectionsExceededException("동일한 모집글에 대해서 3번이상 거절 당했으므로, 더 이상 참가 신청할 수 없는 사용자입니다.");
        }

        // 해당 유저의 참가신청 상태 수정
        participationApplication.assignParticipationApplicationStatus(ParticipationApplicationStatus.NONE);

        // 참가신청 거절 처리
        int newRejectedParticipationCount = participationApplication.getRejectedParticipationCount() + 1;

        // 거절된 참여 횟수가 3번이면 참가 불가 상태로 설정하고 참가 신청을 취소 처리
        if (newRejectedParticipationCount >= 3) {
            participationApplication.assignParticipationApplicationStatus(ParticipationApplicationStatus.REJECTED);
            // 거절된 참여 횟수 업데이트
        } else {
            participationApplication.assignRejectedParticipationCount(newRejectedParticipationCount);
        }
    }

    /**
     * 모집글 참가 신청을 취소 처리
     *
     * @param gatherArticleId 모집글 Id
     * @param username 참가신청을 취소하는 사용자 아이디
     **/
    @Transactional
    public Boolean cancelParticipationApplication(Long gatherArticleId, String username) {

        GatherArticle gatherArticle = gatherArticleRepository.findById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleNotFoundException("해당 모집글이 존재하지 않습니다."));

        MemberGatherArticle memberGatherArticle = memberGatherArticleRepository.findByGatherArticleIdAndMemberUsername(gatherArticleId, username)
                .orElseThrow(() -> new MemberGatherArticleRetrievalException("서버 문제로 해당 모집글 관련한 사용자의 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));

        ParticipationApplication participationApplication = participationApplicationRepository.findByGatherArticleIdAndMemberUsername(gatherArticleId, username)
                .orElseThrow(() -> new ParticipationApplicationRetrievalException("서버 문제로 참가 신청 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));

        // 참가 신청이 이미 취소된 경우 확인
        if (participationApplication.getParticipationApplicationStatus() == ParticipationApplicationStatus.CANCELED) {
            throw new AlreadyCancelledParticipantException("이미 참가 취소된 모집글 입니다.");
        }

        // 사용자의 참가 신청 상태가 대기 또는 승인 상태인지 확인
        if (participationApplication.getParticipationApplicationStatus() != ParticipationApplicationStatus.PENDING &&
                participationApplication.getParticipationApplicationStatus() != ParticipationApplicationStatus.APPROVED) {
            throw new ParticipationApplicationNotPendingOrApproveException("참가신청 대기 또는 승인 상태가 아니므로 참가신청을 취소할 수 없습니다.");
        }

        // 모집글 작성자는 참가 취소를 할 수 없음
        if (memberGatherArticle.getMemberGatherArticleRole() == MemberGatherArticleRole.AUTHOR) {
            throw new AuthorCannotCancelApplicationException("모집글 작성자는 참가 신청을 취소할 수 없습니다.");
        }

        // 참가 신청 취소 처리
        participationApplication.assignParticipationApplicationStatus(ParticipationApplicationStatus.CANCELED);

        boolean isMemberParticipant = false;

        if (memberGatherArticle.getMemberGatherArticleRole() == MemberGatherArticleRole.PARTICIPANT) {
            isMemberParticipant = true;
            memberGatherArticle.assignMemberGatherArticleRole(MemberGatherArticleRole.NONE);
        }

        // 모집글의 현재 참가자 수 업데이트
        gatherArticle.assignCurrentParticipants(gatherArticle.getCurrentParticipants() - 1);

        // 모집글 상태 확인, 업데이트
        updateGatherArticleStatusBasedOnParticipants(gatherArticle);

        return isMemberParticipant;
    }

    /**
     * 모집글 작성자가 회원의 참가 신청 목록 조회
     *
     * @param gatherArticleId 모집글 Id
     * @param username 참가 신청 목록을 조회하는 모집글 작성자 아이디
     * @return 모집글의 모든 참가 신청 목록
     **/
    public List<ParticipationApplicationResponse.InfoDTO> getParticipationAppliedMemberList(Long gatherArticleId, String username) {
        // 모집글 조회
        boolean isGatherArticleExists = gatherArticleRepository.existsById(gatherArticleId);
        if (!isGatherArticleExists) {
            throw new GatherArticleRetrievalException("서버문제로 해당 모집글을 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        // 작성자인지 확인
        Boolean isMemberAuthorOfGatherArticle = gatherArticleRepository.isMemberAuthorOfGatherArticle(gatherArticleId, username);

        if (!isMemberAuthorOfGatherArticle) {
            throw new NotAuthorOfGatherArticleException("해당 모집글의 작성자가 아니므로 참가신청 목록을 조회할 수 없습니다.");
        }

        // 참가 신청 현황 조회
        return participationApplicationRepository.findParticipationAppliedMemberByGatherArticleId(gatherArticleId);
    }

    /**
     * 모집글 상태 확인, 업데이트
     * @param gatherArticle
     */
    private void updateGatherArticleStatusBasedOnParticipants(GatherArticle gatherArticle) {
        GatherArticleStatus newStatus;

        if (gatherArticle.getCurrentParticipants() >= gatherArticle.getMaxParticipants()) {
            newStatus = GatherArticleStatus.CLOSED;
        } else {
            newStatus = GatherArticleStatus.OPEN;
        }

        // 상태가 바뀌어야 하는 상황에만 업데이트 수행
        if (gatherArticle.getGatherArticleStatus() != newStatus) {
            gatherArticle.assignGatherArticleStatus(newStatus);
        }
    }
}
