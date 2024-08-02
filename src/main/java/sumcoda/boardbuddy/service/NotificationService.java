package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.dto.NotificationResponse;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.entity.Notification;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.exception.sseEmitter.SseEmitterSendErrorException;
import sumcoda.boardbuddy.exception.sseEmitter.SseEmitterSubscribeErrorException;
import sumcoda.boardbuddy.repository.member.MemberRepository;
import sumcoda.boardbuddy.repository.notification.NotificationRepository;
import sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepository;
import sumcoda.boardbuddy.repository.memberGatherArticle.MemberGatherArticleRepository;
import sumcoda.boardbuddy.repository.sseEmitter.SseEmitterRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    // SSE Emitter와 이벤트 캐시를 위한 저장소
    private static final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    /**
     * 유저 로그인 시 SSE Emitter 구독 요청 캐치
     *
     * @param username 로그인 사용자 아이디
     **/
    @Transactional
    public SseEmitter subscribe(String username) {
        // 매 연결마다 고유 Id 부여
        String emitterId = username + "_" + System.currentTimeMillis();

        // SseEmitter 인스턴스 생성
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        // 유저 검증 로직
        boolean memberExist = memberRepository.existsByUsername(username);
        if (!memberExist) {
            throw new MemberNotFoundException("존재하지 않는 유저입니다.");
        }

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
        MemberResponse.UserNameDTO authorUsernameDTO = memberGatherArticleRepository.findAuthorUsernameByGatherArticleId(gatherArticleId)
                .orElseThrow(() -> new MemberRetrievalException("서버 문제로 해당 모집글의 작성자를 찾을 수 없습니다. 관리자에게 문의하세요."));

        String authorUsername = authorUsernameDTO.getUsername();

        // 참가 신청 메시지를 포맷하여 생성
        String message = String.format("%s 님이 '%s'에 참가 신청을 했습니다.", getNickname(appliedUsername), formattingTitle(gatherArticleId));

        saveNotification(authorUsername, message, "applyParticipationApplication");
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
        String message = String.format("'%s'의 참가 신청이 승인되었습니다.", formattingTitle(gatherArticleId));

        saveNotification(receiverUsername, message, "approveParticipationApplication");
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
        String message = String.format("'%s'의 참가 신청이 거절되었습니다.", formattingTitle(gatherArticleId));

        saveNotification(receiverUsername, message, "rejectParticipationApplication");
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
        MemberResponse.UserNameDTO authorUsernameDTO = memberGatherArticleRepository.findAuthorUsernameByGatherArticleId(gatherArticleId)
                .orElseThrow(() -> new MemberRetrievalException("서버 문제로 해당 모집글의 작성자를 찾을 수 없습니다. 관리자에게 문의하세요."));

        String authorUsername = authorUsernameDTO.getUsername();

        // 참가 신청 취소 메시지를 포맷하여 생성
        String message = String.format("%s 님이 '%s'의 참가 신청을 취소했습니다.", getNickname(canceledUsername), formattingTitle(gatherArticleId));

        saveNotification(authorUsername, message, "cancelParticipationApplication");
    }

    /**
     * 모집글 상태가 completed로 변경되면 모든 참가자에게 리뷰 요청 알림 보내기
     *
     * @param gatherArticleId 해당 모집글 Id
     **/
    @Transactional
    public void notifyReviewRequest(Long gatherArticleId) {
        // 리뷰 요청 메시지를 포맷하여 생성
        String message = String.format("모집글 '%s'에 대한 리뷰를 남겨주세요!", formattingTitle(gatherArticleId));

        // 모든 참가자들의 아이디 조회
        List<MemberResponse.UserNameDTO> participants = memberGatherArticleRepository.findParticipantsByGatherArticleId(gatherArticleId);

        // 모든 참가자에게 알림 전송
        participants.forEach(userNameDTO -> saveNotification(userNameDTO.getUsername(), message, "reviewRequest"));
    }

    /**
     * 유저의 알림을 조회하여 최신순으로 반환하는 메서드
     *
     * @param username 알림을 조회할 유저의 아이디
     * @return NotificationResponse 알림 응답 DTO
     **/
    public List<NotificationResponse.NotificationDTO> getNotifications(String username) {
        // 유저 검증
        if (Boolean.FALSE.equals(memberRepository.existsByUsername(username))) {
            throw new MemberNotFoundException("해당 유저를 찾을 수 없습니다.");
        }

        //DB에서 해당 유저의 알림을 최신순으로 조회
        return notificationRepository.findNotificationByMemberUsername(username).stream()
                .map(notification -> NotificationResponse.NotificationDTO.builder()
                        .message(notification.getMessage())
                        .createdAt(notification.getCreatedAt())
                        .build())
                .toList();
    }

    /**
     * 알림 생성 시 DB에 저장하는 메서드
     *
     * @param username 알림을 받는 유저의 아이디
     * @param message 알림 메세지
     * @param eventName 알림 이벤트 이름
     **/
    @Transactional
    public void saveNotification(String username, String message, String eventName) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        Notification notification = Notification.buildNotification(message, LocalDateTime.now(), member);
        notificationRepository.save(notification);

        sendNotification(username, message, eventName);
    }

    /**
     * 유저가 SSE Emitter에 등록되어 있는지 확인하고 DB에 저장 후 알림을 보내는 메서드
     *
     * @param username 알림을 받는 유저의 아이디
     * @param message 알림 메세지
     * @param eventName 알림 이벤트 이름
     **/
    private void sendNotification(String username, String message, String eventName) {
        // 작성자가 SSE 이벤트 수신을 위해 등록되어 있는지 확인
        if (emitters.containsKey(username)) {

            // 작성자의 SSE Emitter 객체를 가져옴
            SseEmitter sseEmitterReceiver = emitters.get(username);

            try {
                // 알림 메세지를 SSE Emitter를 통해 전송
                sseEmitterReceiver.send(SseEmitter.event().name(eventName).data(message));

                // 이벤트 캐시에 알림 메세지를 저장
                String eventCacheId = username + "_" + System.currentTimeMillis();
                sseEmitterRepository.saveEventCache(eventCacheId, message);

            } catch (SseEmitterSendErrorException | IOException e) {
                // 전송 중 오류 발생 시, 작성자의 SSE Emitter를 제거
                emitters.remove(username);
                log.error("알림 전송 에러: {}. emitter 제거.", e.getMessage());
            }
        }
    }

    /**
     * 모집글 Id로 모집글 제목을 가져와 10글자 이상이면 포맷팅하여 반환하는 메서드
     *
     * @param gatherArticleId 해당 모집글 Id
     * @return title 포맷팅한 제목
     **/
    private String formattingTitle(Long gatherArticleId) {

        // 모집글 조회
        GatherArticleResponse.TitleDTO gatherArticle = gatherArticleRepository.findTitleDTOById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleNotFoundException("존재하지 않는 모집글입니다."));

        // 해당 모집글 제목 가져오기
        String gatherArticleTitle = gatherArticle.getTitle();

        return gatherArticleTitle.length() > 9 ? gatherArticleTitle.substring(0,9) + "..." : gatherArticleTitle;
    }

    /**
     * 유저 아이디로 조회하여 유저 닉네임을 반환하는 메서드
     *
     * @param username 유저 아이디
     * @return nickname 유저 닉네임
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
     * @return username 유저 아이디
     **/
    private String getUsername(String nickname) {

        // 유저 닉네임으로 유저 아이디 조회
        MemberResponse.UserNameDTO userNameDTO = memberRepository.findUsernameDTOByNickname(nickname)
                .orElseThrow(() -> new MemberRetrievalException("서버 문제로 해당 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        return userNameDTO.getUsername();
    }
}
