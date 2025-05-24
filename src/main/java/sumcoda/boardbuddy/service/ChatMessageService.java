package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Pair;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.ChatMessageRequest;
import sumcoda.boardbuddy.dto.ChatMessageResponse;
import sumcoda.boardbuddy.dto.common.PageResponse;
import sumcoda.boardbuddy.entity.ChatMessage;
import sumcoda.boardbuddy.entity.ChatRoom;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.enumerate.MessageType;
import sumcoda.boardbuddy.exception.*;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.infra.event.*;
import sumcoda.boardbuddy.repository.chatMessage.ChatMessageRepository;
import sumcoda.boardbuddy.repository.chatRoom.ChatRoomRepository;
import sumcoda.boardbuddy.repository.member.MemberRepository;
import sumcoda.boardbuddy.repository.memberChatRoom.MemberChatRoomRepository;
import sumcoda.boardbuddy.util.ChatMessageUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static sumcoda.boardbuddy.util.ChatMessageUtil.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final MemberRepository memberRepository;

    private final MemberChatRoomRepository memberChatRoomRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 메세지 발행 및 채팅방에 메세지 전송
     *
     * @param chatRoomId 채팅방 Id
     * @param publishDTO 발행 및 전송할 메시지 내용, 메시지 발행 및 전송 사용자 닉네임
     **/
    @Transactional
    public void publishMessage(Long chatRoomId, ChatMessageRequest.PublishDTO publishDTO) {


        log.info("[서비스] 채팅방 ID={} | 닉네임={} | 내용={}", chatRoomId, publishDTO.getNickname(), publishDTO.getContent());


        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방이 존재하지 않습니다."));

        String nickname = publishDTO.getNickname();

        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new MemberNotFoundException("해당 사용자를 찾을 수 없습니다."));

        log.info("[검증 완료] 유저={} (채팅방={})", member.getNickname(), chatRoom.getId());

        Boolean isMemberChatRoomExists = memberChatRoomRepository.existsByChatRoomIdAndMemberNickname(chatRoomId, nickname);
        if (!isMemberChatRoomExists) {
            throw new MemberChatRoomRetrievalException("서버 문제로 해당 채팅방의 사용자 정보를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        String content = publishDTO.getContent();

//        ChatMessage chatMessage = ChatMessage.buildChatMessage(content, MessageType.TALK, member, chatRoom);

        // 성능 개선용
        ChatMessage chatMessage = ChatMessage.buildChatMessage(content, "TALK", member, chatRoom);
        Long chatMessageId = chatMessageRepository.save(chatMessage).getId();

        log.info("[DB 저장 완료] 채팅 메시지 ID={} | 내용={}", chatMessageId, publishDTO.getContent());

        if (chatMessageId == null) {
            throw new ChatMessageSaveException("서버 문제로 메세지를 저장할 수 없습니다. 관리자에게 문의하세요.");
        }

        ChatMessageResponse.ChatMessageItemInfoProjectionDTO responseChatMessage = chatMessageRepository.findTalkMessageById(chatMessageId)
                .orElseThrow(() -> new ChatMessageRetrievalException("서버 문제로 해당 메세지를 찾을 수 없습니다. 관리자에게 문의하세요."));

        ChatMessageResponse.ChatMessageItemInfoDTO payload = convertPayload(responseChatMessage);

        try {
            // 메시지 전송 시도 이벤트 발행
            applicationEventPublisher.publishEvent(new ChatMessageProcessingStartedEvent());

            // 메시지 전송 처리 시간 측정 시작 (나노초 단위)
            long startTime = System.nanoTime();

            // 채팅방 구독자들에게 메시지 전송 (STOMP SEND)
            messagingTemplate.convertAndSend("/ws/chat/messages/subscription/" + chatRoomId, payload);

            // 메시지 전송 처리 시간 측정 종료
            double durationMillis = (System.nanoTime() - startTime) / 1_000_000.0;

            // 메시지 전송 성공 이벤트 발행
            applicationEventPublisher.publishEvent(new ChatMessageProcessingCompletedEvent(durationMillis));

        } catch (Exception e) {
            log.error("STOMP 메시지 처리 중 예외 발생: {}", e.getMessage());

            throw e; // 메시지 전송시 예외 발생시 DB에 메시지를 저장한 작업 같은 것들을 롤백
        }
    }





    /**
     * 채팅방 입장/퇴장 메세지 발행 및 채팅방에 사용자 입장/퇴장 메세지 전송
     *
     * @param chatRoomIdAndNicknamePair 메세지 발행 및 전송할 채팅방 Id, 메세지 발행 및 전송 사용자 닉네임
     * @param messageType 메세지 유형 (입장/퇴장)
     **/
    @Transactional
    public void publishEnterOrExitChatMessage(Pair<Long, String> chatRoomIdAndNicknamePair, MessageType messageType) {

        Long chatRoomId = chatRoomIdAndNicknamePair.getFirst();

        String nickname = chatRoomIdAndNicknamePair.getSecond();

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방이 존재하지 않습니다."));

        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new MemberRetrievalException("해당 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        Boolean isMemberChatRoomExists = memberChatRoomRepository.existsByChatRoomIdAndMemberNickname(chatRoomId, nickname);
        if (!isMemberChatRoomExists) {
            throw new MemberChatRoomRetrievalException("서버 문제로 해당 채팅방의 사용자 정보를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        String content = ChatMessageUtil.buildChatMessageContent(nickname, messageType);

//        ChatMessage chatMessage = ChatMessage.buildChatMessage(content, messageType, member, chatRoom);

        // 성능 개선용
        ChatMessage chatMessage = ChatMessage.buildChatMessage(content, "ENTER", member, chatRoom);

        Long chatMessageId = chatMessageRepository.save(chatMessage).getId();

        if (chatMessageId == null) {
            throw new ChatMessageSaveException("서버 문제로 메세지를 저장할 수 없습니다. 관리자에게 문의하세요.");
        }

        ChatMessageResponse.ChatMessageItemInfoProjectionDTO responseChatMessage = chatMessageRepository.findEnterOrExitMessageById(chatMessageId)
                .orElseThrow(() -> new ChatMessageRetrievalException("서버 문제로 해당 메세지를 찾을 수 없습니다. 관리자에게 문의하세요."));

        ChatMessageResponse.ChatMessageItemInfoDTO payload = convertPayload(responseChatMessage);

        // 채팅방 구독자들에게 메시지 전송
        messagingTemplate.convertAndSend("/ws/chat/messages/subscription/" + chatRoomId, payload);
    }

    /**
     * 사용자가 채팅방에 입장한 이후 최신 N개 메시지를 커서 기반으로 조회
     *
     * @param chatRoomId 조회할 채팅방 ID
     * @param username 조회할 사용자 이름
     * @return 최신 메시지 페이징 결과 (hasMore, nextCursor 포함)
     * @throws ChatRoomNotFoundException 채팅방이 존재하지 않을 때
     * @throws ChatRoomAccessDeniedException 사용자가 채팅방에 속하지 않을 때
     * @throws ChatMessageRetrievalException 조회된 메시지가 없거나 조회에 실패할 때
     * @since 1.0
     * @version 2.0
     */
    public PageResponse<ChatMessageResponse.ChatMessageItemInfoDTO> findInitialChatMessages(Long chatRoomId, String username) {

        // 채팅방 입장 검증
        validateChatRoomAccess(chatRoomId, username);

        LocalDateTime joinedAt = findJoinedAt(chatRoomId, username);

        // 페이지 조회
        List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> chatMessageItemList =
                chatMessageRepository.findInitialMessagesByChatRoomIdAndUsernameAndJoinedAt(chatRoomId, username, joinedAt);

        // 2) hasMore 계산 & 실제 목록 추출
        Boolean hasMore = getHasMore(chatMessageItemList.size());

        List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> subChatMessageItemList = getSubChatMessageItemInfoProjectionDTOList(chatMessageItemList, hasMore);

        // 3) ASC 순서로 통일
        Collections.reverse(subChatMessageItemList);

        // 과거(older) 페이지 네이션을 위한 nextCursor 계산
        // dataList는 ASC(오래된→최신) 순으로 정렬되어 있으므로,
        // 가장 오래된 메시지(dataList.get(0))를 기준 커서로 사용함.
        String nextCursor = getNextCursor(subChatMessageItemList, hasMore, false);

        // 클라이언트 제공 형태로 변환
        return convertToChatMessagePageResponse(subChatMessageItemList, hasMore, nextCursor);
    }

    /**
     * WS 재연결 등으로 누락된 최신 메시지를 커서 이후부터 조회
     *
     * @param chatRoomId 조회할 채팅방 ID
     * @param username 조회할 사용자 이름
     * @param cursor 마지막 메시지를 가리키는 opaque cursor("sentAt_id")
     * @return 누락된 최신 메시지 페이징 결과 (hasMore, nextCursor 포함)
     * @throws ChatRoomNotFoundException 채팅방이 존재하지 않을 때
     * @throws ChatRoomAccessDeniedException 사용자가 채팅방에 속하지 않을 때
     * @throws ChatMessageRetrievalException 조회된 메시지가 없거나 조회에 실패할 때
     * @since 1.0
     * @version 2.0
     */
    public PageResponse<ChatMessageResponse.ChatMessageItemInfoDTO> findNewerChatMessages(Long chatRoomId, String username, String cursor) {

        // 채팅방 입장 검증
        validateChatRoomAccess(chatRoomId, username);

        LocalDateTime joinedAt = findJoinedAt(chatRoomId, username);

        // 커서 파싱
        Pair<LocalDateTime, Long> parsed = parseCursor(cursor);
        LocalDateTime cursorSentAt = parsed.getFirst();
        Long cursorId = parsed.getSecond();

        // 페이지 조회
        List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> chatMessageItemList =
                chatMessageRepository.findNewerMessagesByChatRoomIdAndUsernameAndJoinedAtAndCursor(
                        chatRoomId, username, joinedAt, cursorSentAt, cursorId);

        // 3) hasMore 계산 & 목록 추출 (ASC 그대로)
        Boolean hasMore = getHasMore(chatMessageItemList.size());

        List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> subChatMessageItemList = getSubChatMessageItemInfoProjectionDTOList(chatMessageItemList, hasMore);

        // nextCursor 생성 (마지막 요소 기준)
        String nextCursor = getNextCursor(subChatMessageItemList, hasMore, true);

        // 클라이언트 제공 형태로 변환
        return convertToChatMessagePageResponse(subChatMessageItemList, hasMore, nextCursor);
    }

    /**
     * 스크롤 업 등으로 과거 메시지를 커서 이전부터 조회
     *
     * @param chatRoomId 조회할 채팅방 ID
     * @param username 조회할 사용자 이름
     * @param cursor 과거 조회 기준이 되는 opaque cursor("sentAt_id")
     * @return 과거 메시지 페이징 결과 (hasMore, nextCursor 포함)
     * @throws ChatRoomNotFoundException     채팅방이 존재하지 않을 때
     * @throws ChatRoomAccessDeniedException 사용자가 채팅방에 속하지 않을 때
     * @throws ChatMessageRetrievalException 조회된 메시지가 없거나 조회에 실패할 때
     * @since 1.0
     * @version 2.0
     */
    public PageResponse<ChatMessageResponse.ChatMessageItemInfoDTO> findOlderChatMessages(Long chatRoomId, String username, String cursor) {

        // 채팅방 입장 검증
        validateChatRoomAccess(chatRoomId, username);

        LocalDateTime joinedAt = findJoinedAt(chatRoomId, username);

        // 커서 파싱
        Pair<LocalDateTime, Long> parsed = parseCursor(cursor);
        LocalDateTime cursorSentAt = parsed.getFirst();
        Long cursorId = parsed.getSecond();

        // 페이지 조회
        List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> chatMessageItemList =
                chatMessageRepository.findOlderMessagesByChatRoomIdAndUsernameAndJoinedAtAndCursor(
                        chatRoomId, username, joinedAt, cursorSentAt, cursorId);

        // 3) hasMore 계산 & 목록 추출
        Boolean hasMore = getHasMore(chatMessageItemList.size());

        List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> subChatMessageItemList = getSubChatMessageItemInfoProjectionDTOList(chatMessageItemList, hasMore);

        // 4) ASC 순서로 통일
        Collections.reverse(subChatMessageItemList);

        String nextCursor = getNextCursor(subChatMessageItemList, hasMore, false);

        // 클라이언트 제공 형태로 변환
        return convertToChatMessagePageResponse(subChatMessageItemList, hasMore, nextCursor);
    }

    /**
     * 채팅방 존재 여부와 사용자 접근 권한을 검증
     *
     * @param chatRoomId 조회할 채팅방 ID
     * @param username 조회할 사용자 이름
     * @throws ChatRoomNotFoundException 채팅방이 존재하지 않을 때
     * @throws ChatRoomAccessDeniedException 사용자가 채팅방에 속하지 않을 때
     * @since 1.0
     * @version 2.0
     */
    private void validateChatRoomAccess(Long chatRoomId, String username) {
        if (!chatRoomRepository.existsById(chatRoomId)) {
            throw new ChatRoomNotFoundException("입장하려는 채팅방을 찾을 수 없습니다.");
        }
        if (!memberChatRoomRepository.existsByChatRoomIdAndMemberUsername(chatRoomId, username)) {
            throw new ChatRoomAccessDeniedException("해당 채팅방에 입장하지 않은 사용자입니다.");
        }
    }




    /**
     * 주어진 채팅방 ID와 사용자 이름으로 memberChatRoom 테이블에서 사용자의 입장 시각을 조회
     *
     * @param chatRoomId 조회할 채팅방 ID
     * @param username 조회할 사용자 이름
     * @return 사용자가 채팅방에 입장한 시각
     * @throws MemberChatRoomRetrievalException 조회된 입장 시각이 null인 경우 발생
     */
    private LocalDateTime findJoinedAt(Long chatRoomId, String username) {
        LocalDateTime joinedAt = chatMessageRepository.findJoinedAtByChatRoomIdAndUsername(chatRoomId, username);

        isJoinedAtExists(joinedAt);

        return joinedAt;
    }

    /**
     * 조회된 joinedAt 값이 null인지 검사하고, null일 경우 예외를 발생
     *
     * @param joinedAt 조회된 입장 시각
     * @throws MemberChatRoomRetrievalException joinedAt이 null인 경우 발생
     */
    private static void isJoinedAtExists(LocalDateTime joinedAt) {
        if (joinedAt == null) {
            throw new MemberChatRoomRetrievalException("서버 문제로 사용자가 해당 채팅방에 입장한 시간을 찾을 수 없습니다. 관리자에게 문의하세요.");
        }
    }

//    /**
//     * 사용자가 채팅방에 입장한 이후의 메세지 조회(V1)
//     *
//     * @param chatRoomId 채팅방 Id
//     * @param username 사용자 아이디
//     * @return 사용자가 입장한 이후의 채팅방 메시지 목록
//     * @version 1.0
//     **/
//    public List<ChatMessageResponse.ChatMessageItemInfoDTO> findChatMessagesAfterMemberJoinedAt(Long chatRoomId, String username) {
//
//        boolean isChatRoomExists = chatRoomRepository.existsById(chatRoomId);
//
//        if (!isChatRoomExists) {
//            throw new ChatRoomNotFoundException("입장하려는 채팅방을 찾을 수 없습니다.");
//        }
//
//        Boolean isMemberChatRoomExists = memberChatRoomRepository.existsByChatRoomIdAndMemberUsername(chatRoomId, username);
//        if (!isMemberChatRoomExists) {
//            throw new ChatRoomAccessDeniedException("해당 채팅방에 입장하지 않은 사용자입니다.");
//        }
//
//        // 채팅방 메시지 조회 로직
//        List<ChatMessageResponse.ChatMessageItemInfoDTO> messages = chatMessageRepository.findMessagesAfterMemberJoinedByChatRoomIdAndUsername(chatRoomId, username);
//
//        if (messages.isEmpty()) {
//            throw new ChatMessageRetrievalException("서버 문제로 메시지를 조회하지 못하였습니다. 관리자에게 문의하세요");
//        }
//
//        return messages.stream()
//                .map(message -> {
//                    ChatMessageResponse.ChatMessageItemInfoDTO.ChatMessageItemInfoDTOBuilder builder =
//                            ChatMessageResponse.ChatMessageItemInfoDTO.builder()
//                                    .content(message.getContent())
//                                    .messageType(message.getMessageType());
//
////                    if (message.getMessageType() == MessageType.TALK) {
////                        builder.nickname(message.getNickname())
////                                .profileImageS3SavedURL(message.getProfileImageS3SavedURL())
////                                .rank(message.getRank())
////                                .sentAt(message.getSentAt());
////                    }
//
//                    // 성능 개선용
//                    if (Objects.equals(message.getMessageType(), "TALK")) {
//                        builder.nickname(message.getNickname())
//                                .profileImageS3SavedURL(message.getProfileImageS3SavedURL())
//                                .rank(message.getRank())
//                                .sentAt(message.getSentAt());
//                    }
//
//                    return builder.build();
//                })
//                .collect(Collectors.toList());
//    }
}