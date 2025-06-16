package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import sumcoda.boardbuddy.dto.CommentResponse;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.dto.NotificationResponse;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.entity.Notification;
import sumcoda.boardbuddy.enumerate.EventName;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleNotFoundException;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleRetrievalException;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.exception.sseEmitter.SseEmitterSendErrorException;
import sumcoda.boardbuddy.exception.sseEmitter.SseEmitterSubscribeErrorException;
import sumcoda.boardbuddy.repository.member.MemberRepository;
import sumcoda.boardbuddy.repository.comment.CommentRepository;
import sumcoda.boardbuddy.repository.notification.NotificationRepository;
import sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepository;
import sumcoda.boardbuddy.repository.memberGatherArticle.MemberGatherArticleRepository;
import sumcoda.boardbuddy.repository.sseEmitter.SseEmitterRepository;
import sumcoda.boardbuddy.util.NotificationMessageUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotificationService {

    private final MemberRepository memberRepository;

    private final SseEmitterRepository sseEmitterRepository;

    private final GatherArticleRepository gatherArticleRepository;

    private final MemberGatherArticleRepository memberGatherArticleRepository;

    private final NotificationRepository notificationRepository;

    private final CommentRepository commentRepository;

    private final NotificationMessageUtil notificationMessageUtil;

    // SSE Emitter와 이벤트 캐시를 위한 저장소
    private static final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    /**
     * 유저 로그인 시 SSE Emitter 구독 요청 캐치
     *
//     * @param nickname 알람 구독 요청 사용자 닉네임
     **/
    @Transactional
    public SseEmitter subscribe(String username) {

//        MemberResponse.UsernameDTO usernameDTO = memberRepository.findUsernameDTOByNickname(nickname)
//                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 유저입니다."));
//
//        String username = usernameDTO.getUsername();

        // 매 연결마다 고유 Id 부여
        String emitterId = username + "_" + System.currentTimeMillis();

        // SseEmitter 인스턴스 생성
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);


        // 503 Service Unavailable 방지용 dummy 이벤트 전송
        try {
            emitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            throw new SseEmitterSubscribeErrorException("서버 문제로 알림 구독을 실패했습니다. 관리자에게 문의하세요.");
        }

        emitters.put(username, emitter);
        log.info("SSE Emitter for user {} added with ID: {}", username, emitterId);

        // 알림 전송 완료, 타임 아웃 시 emitter 삭제 처리
        emitter.onCompletion(() -> sseEmitterRepository.deleteEmitterById(emitterId));
        emitter.onTimeout(() -> sseEmitterRepository.deleteEmitterById(emitterId));

        return emitter;
    }

    /**
     * 참가 신청 시 모집글 작성자에게 알림 보내기
     *
     * @param gatherArticleId 해당 모집글 Id
     * @param appliedUsername 참가 신청한 유저 아이디
     **/
    @Transactional
    public void notifyApplyParticipation(Long gatherArticleId, String appliedUsername) {
        // 모집글 작성자의 유저 아이디를 조회
        MemberResponse.UsernameDTO authorUsernameDTO = memberGatherArticleRepository.findAuthorUsernameByGatherArticleId(gatherArticleId)
                .orElseThrow(() -> new MemberRetrievalException("서버 문제로 해당 모집글의 작성자를 찾을 수 없습니다. 관리자에게 문의하세요."));

        String authorUsername = authorUsernameDTO.getUsername();

        // 참가 신청 메시지를 포맷하여 생성
        String message = notificationMessageUtil.formatApplyParticipationMessage(
                getNickname(appliedUsername),
                getTitle(gatherArticleId));

        saveNotification(authorUsername, message, EventName.APPLY_PARTICIPATION);
    }

    /**
     * 참가 신청 승인 시 신청한 유저에게 알림 보내기
     *
     * @param gatherArticleId 해당 모집글 Id
     * @param appliedNickname 참가 신청한 유저 닉네임
     **/
    @Transactional
    public void notifyApproveParticipation(String appliedNickname, Long gatherArticleId) {
        // 참가 신청한 유저의 닉네임으로 유저 아이디 조회
        String receiverUsername = getUsername(appliedNickname);

        // 참가 신청 승인 메시지를 포맷하여 생성
        String message = notificationMessageUtil.formatApproveParticipationMessage(
                getTitle(gatherArticleId));

        saveNotification(receiverUsername, message, EventName.APPROVE_PARTICIPATION);
    }

    /**
     * 참가 신청 거절 시 신청한 유저에게 알림 보내기
     *
     * @param gatherArticleId 해당 모집글 Id
     * @param appliedNickname 참가 신청한 유저 닉네임
     **/
    @Transactional
    public void notifyRejectParticipation(String appliedNickname, Long gatherArticleId) {
        // 참가 신청한 유저의 닉네임으로 유저 아이디 조회
        String receiverUsername = getUsername(appliedNickname);

        // 참가 신청 거절 메시지를 포맷하여 생성
        String message = notificationMessageUtil.formatRejectParticipationMessage(
                getTitle(gatherArticleId));

        saveNotification(receiverUsername, message, EventName.REJECT_PARTICIPATION);
    }

    /**
     * 참가 신청 취소 시 모집글 작성자에게 알림 보내기
     *
     * @param gatherArticleId 해당 모집글 Id
     * @param canceledUsername 참가 신청 취소한 유저 아이디
     **/
    @Transactional
    public void notifyCancelParticipation(Long gatherArticleId, String canceledUsername) {
        // 모집글 작성자의 유저 아이디를 조회
        MemberResponse.UsernameDTO authorUsernameDTO = memberGatherArticleRepository.findAuthorUsernameByGatherArticleId(gatherArticleId)
                .orElseThrow(() -> new MemberRetrievalException("서버 문제로 해당 모집글의 작성자를 찾을 수 없습니다. 관리자에게 문의하세요."));

        String authorUsername = authorUsernameDTO.getUsername();

        // 참가 신청 취소 메시지를 포맷하여 생성
        String message = notificationMessageUtil.formatCancelParticipationMessage(
                getNickname(canceledUsername),
                getTitle(gatherArticleId));

        saveNotification(authorUsername, message, EventName.CANCEL_PARTICIPATION);
    }

    /**
     * 모집글 상태가 completed로 변경되면 모든 참가자에게 리뷰 요청 알림 보내기
     *
     * @param gatherArticleId 해당 모집글 Id
     **/
    @Transactional
    public void notifyReviewRequest(Long gatherArticleId) {
        // 리뷰 요청 메시지를 포맷하여 생성
        String message = notificationMessageUtil.formatReviewRequestMessage(
                getTitle(gatherArticleId));

        // 모든 참가자들의 아이디 조회
        List<MemberResponse.UsernameDTO> participants = memberGatherArticleRepository.findParticipantsByGatherArticleId(gatherArticleId);

        // 모든 참가자에게 알림 전송
        participants.forEach(userNameDTO -> saveNotification(userNameDTO.getUsername(), message, EventName.REVIEW_REQUEST));
    }

    /**
     * 모집글에 댓글이 달리면 모집글 작성자에게, 대댓글이 달리면 원댓글 작성자에게 알림 보내기
     *
     * @param gatherArticleId 해당 모집글 Id
     * @param parentId 원댓글 id
     * @param writtenUsername 댓글 작성 유저 아이디
     **/
    @Transactional
    public void notifyWriteComment(Long gatherArticleId, Long parentId, String writtenUsername) {

        if (parentId == null) {
            // 모집글 작성자의 유저 아이디를 조회
            MemberResponse.UsernameDTO authorUsernameDTO = memberGatherArticleRepository.findAuthorUsernameByGatherArticleId(gatherArticleId)
                    .orElseThrow(() -> new MemberRetrievalException("서버 문제로 해당 모집글의 작성자를 찾을 수 없습니다. 관리자에게 문의하세요."));

            String authorUsername = authorUsernameDTO.getUsername();

            // 댓글 작성자와 모집글 작성자가 다를때만 알림 전송
            if (!Objects.equals(writtenUsername, authorUsername)) {
                // 댓글 알림 메시지를 포맷하여 생성
                String message = notificationMessageUtil.formatWriteCommentMessage(
                        getNickname(writtenUsername),
                        getTitle(gatherArticleId));

                saveNotification(authorUsername, message, EventName.WRITE_COMMENT);
            }

        } else {
            // 원댓글 작성자의 유저 아이디를 조회
            Optional<CommentResponse.AuthorUsernameDTO> authorUsernameDTO = commentRepository.findCommentAuthorByCommentId(parentId);

            String authorUsername = authorUsernameDTO.get().getUsername();

            // 대댓글 작성자와 원댓글 작성자가 다를때만 알림 전송
            if (!authorUsername.equals(writtenUsername)) {
                // 대댓글 알림 메시지를 포맷하여 생성
                String message = notificationMessageUtil.formatReplyCommentMessage(
                        getNickname(writtenUsername),
                        getTitle(gatherArticleId));

                saveNotification(authorUsername, message, EventName.WRITE_COMMENT);
            }
        }
    }

    /**
     * @apiNote 현재는 사용률 저조로 기능이 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가 시 다시 활성화될 수 있음
     * 모집글이 작성되면 해당 모집글 주변에 위치한 사용자에게 알림
     *
     * @param gatherArticleId 해당 모집글 Id
     **/
//    @Transactional
//    public void notifyGatherArticle(Long gatherArticleId, String writtenUsername) {
//
//        GatherArticleResponse.LocationInfoDTO locationInfoDTO = gatherArticleRepository.findLocationInfoDTOById(gatherArticleId)
//                .orElseThrow(() -> new GatherArticleRetrievalException("서버 문제로 해당 모집글의 정보를 찾을 수 없습니다. 관리자에게 문의하세요."));
//
//        String sido = locationInfoDTO.getSido();
//        String sgg = locationInfoDTO.getSgg();
//        String emd = locationInfoDTO.getEmd();
//
//        List<String> usernamesWithGatherArticleInRange = memberRepository.findUsernamesWithGatherArticleInRange(writtenUsername, sido, sgg, emd);
//
//        for (String username : usernamesWithGatherArticleInRange) {
//            // 리뷰 요청 메시지를 포맷하여 생성
//            String message = notificationMessageUtil.formatWriteGatherArticleMessage(
//                    getNickname(username),
//                    getTitle(gatherArticleId));
//
//            log.info(message);
//
//            saveNotification(username, message, EventName.WRITE_GATHER_ARTICLE);
//        }
//    }

    /**
     * 유저의 알림을 조회하여 최신순으로 반환하는 메서드
     *
     * @param username 알림 목록 조회 사용자 아이디
     * @return 알림 응답 DTO
     **/
    public List<NotificationResponse.NotificationDTO> getNotifications(String username) {
        Boolean isMemberExists = memberRepository.existsByUsername(username);

        if (!isMemberExists) {
            throw new MemberNotFoundException("해당 유저를 찾을 수 없습니다.");
        }

        //DB에서 해당 유저의 알림을 최신순으로 조회
        return notificationRepository.findNotificationByMemberUsername(username);
    }

    /**
     * 알림 생성 시 DB에 저장하는 메서드
     *
     * @param username 알림을 받는 유저의 아이디
     * @param message 알림 메세지
     * @param eventName 알림 이벤트 이름
     **/
    public void saveNotification(String username, String message, EventName eventName) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        Notification notification = Notification.buildNotification(message, LocalDateTime.now(), member);
        notificationRepository.save(notification);

        sendNotification(username, message, eventName);
    }

    /**
     * 유저가 SSE Emitter에 등록되어 있는지 확인하고 알림을 보내는 메서드
     *
     * @param username 알림을 받는 유저의 아이디
     * @param message 알림 메세지
     * @param eventName 알림 이벤트 이름
     **/
    private void sendNotification(String username, String message, EventName eventName) {
        // 작성자가 SSE 이벤트 수신을 위해 등록되어 있는지 확인
        if (emitters.containsKey(username)) {

            // 작성자의 SSE Emitter 객체를 가져옴
            SseEmitter sseEmitterReceiver = emitters.get(username);

            try {
                // 알림 메세지를 SSE Emitter를 통해 전송
                sseEmitterReceiver.send(SseEmitter.event().name(String.valueOf(eventName)).data(message));

                // 이벤트 캐시에 알림 메세지를 저장
                String eventCacheId = username + "_" + System.currentTimeMillis();
                sseEmitterRepository.saveEventCache(eventCacheId, message);

            } catch (IllegalStateException | SseEmitterSendErrorException | IOException e) {
                // 전송 중 오류 발생 시, 작성자의 SSE Emitter를 제거
                emitters.remove(username);
                log.error("알림 전송 에러: {}. emitter 제거.", e.getMessage());
            }
        }
    }

    /**
     * 모집글 Id로 모집글 제목 조회
     *
     * @param gatherArticleId 해당 모집글 Id
     * @return 모집글 제목
     **/
    private String getTitle(Long gatherArticleId) {
        // 모집글 조회
        GatherArticleResponse.TitleDTO titleDTO = gatherArticleRepository.findTitleDTOById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleNotFoundException("존재하지 않는 모집글입니다."));

        return titleDTO.getTitle();
    }

    /**
     * 유저 아이디로 조회하여 유저 닉네임을 반환하는 메서드
     *
     * @param username 유저 아이디
     * @return 유저 닉네임
     **/
    private String getNickname(String username) {
        // 유저 아이디로 유저 닉네임 조회
        MemberResponse.NicknameDTO nicknameDTO = memberRepository.findNicknameDTOByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("서버 문제로 해당 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));
        return nicknameDTO.getNickname();
    }

    /**
     * 유저 닉네임으로 조회하여 유저 아이디를 반환하는 메서드
     *
     * @param nickname 유저 닉네임
     * @return 유저 아이디
     **/
    private String getUsername(String nickname) {
        // 유저 닉네임으로 유저 아이디 조회
        MemberResponse.UsernameDTO userNameDTO = memberRepository.findUsernameDTOByNickname(nickname)
                .orElseThrow(() -> new MemberRetrievalException("서버 문제로 해당 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));
        return userNameDTO.getUsername();
    }
}
