package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.ChatMessageRequest;
import sumcoda.boardbuddy.dto.ChatMessageResponse;
import sumcoda.boardbuddy.entity.ChatMessage;
import sumcoda.boardbuddy.entity.ChatRoom;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.enumerate.MessageType;
import sumcoda.boardbuddy.exception.*;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.repository.ChatMessageRepository;
import sumcoda.boardbuddy.repository.chatRoom.ChatRoomRepository;
import sumcoda.boardbuddy.repository.MemberRepository;
import sumcoda.boardbuddy.repository.memberChatRoom.MemberChatRoomRepository;
import sumcoda.boardbuddy.util.ChatMessageUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final MemberRepository memberRepository;

    private final MemberChatRoomRepository memberChatRoomRepository;

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 메세지 발행 및 채팅방에 메세지 전송
     *
     * @param chatRoomId 채팅방 Id
     * @param publishDTO 전송할 메시지 내용
     * @param username 메세지를 전송하는 사용자 아이디
     **/
    @Transactional
    public void publishMessage(Long chatRoomId, ChatMessageRequest.PublishDTO publishDTO, String username) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방이 존재하지 않습니다."));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("해당 사용자를 찾을 수 없습니다."));

        Boolean isMemberChatRoomExists = memberChatRoomRepository.existsByChatRoomIdAndMemberUsername(chatRoomId, username);
        if (!isMemberChatRoomExists) {
            throw new MemberChatRoomRetrievalException("서버 문제로 해당 채팅방의 사용자 정보를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        String content = publishDTO.getContent();

        ChatMessage chatMessage = ChatMessage.buildChatMessage(content, MessageType.TALK, member, chatRoom);

        Long chatMessageId = chatMessageRepository.save(chatMessage).getId();

        if (chatMessageId == null) {
            throw new ChatMessageSaveException("서버 문제로 메세지를 저장할 수 없습니다. 관리자에게 문의하세요.");
        }

        ChatMessageResponse.ChatMessageInfoDTO responseChatMessage = chatMessageRepository.findTalkMessageById(chatMessageId)
                .orElseThrow(() -> new ChatMessageRetrievalException("서버 문제로 해당 메세지를 찾을 수 없습니다. 관리자에게 문의하세요."));

        messagingTemplate.convertAndSend("/api/chat/reception/" + chatRoomId, responseChatMessage);
    }

    /**
     * 채팅방 입장/퇴장 메세지 발행 및 채팅방에 사용자 입장/퇴장 메세지 전송
     *
     * @param chatRoomId 채팅방 Id
     * @param messageType 메세지 유형 (입장/퇴장)
     * @param username 입장하는 사용자 아이디
     **/
    @Transactional
    public void publishEnterOrExitChatMessage(Long chatRoomId, MessageType messageType, String username) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방이 존재하지 않습니다."));

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberRetrievalException("해당 유저를 찾을 수 없습니다. 관리자에게 문의하세요."));

        Boolean isMemberChatRoomExists = memberChatRoomRepository.existsByChatRoomIdAndMemberUsername(chatRoomId, username);
        if (!isMemberChatRoomExists) {
            throw new MemberChatRoomRetrievalException("서버 문제로 해당 채팅방의 사용자 정보를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        String nickname = member.getNickname();

        String content = ChatMessageUtil.buildChatMessageContent(nickname, messageType);

        ChatMessage chatMessage = ChatMessage.buildChatMessage(content, messageType, member, chatRoom);

        Long chatMessageId = chatMessageRepository.save(chatMessage).getId();

        if (chatMessageId == null) {
            throw new ChatMessageSaveException("서버 문제로 메세지를 저장할 수 없습니다. 관리자에게 문의하세요.");
        }

        ChatMessageResponse.EnterOrExitMessageInfoDTO responseChatMessage = chatMessageRepository.findEnterOrExitMessageById(chatMessageId)
                .orElseThrow(() -> new ChatMessageRetrievalException("서버 문제로 해당 메세지를 찾을 수 없습니다. 관리자에게 문의하세요."));

        // 채팅방 구독자들에게 메시지 전송
        messagingTemplate.convertAndSend("/api/chat/reception/" + chatRoomId, responseChatMessage);
    }

    /**
     * 사용자가 채팅방에 입장한 이후의 메세지 조회
     *
     * @param chatRoomId 채팅방 Id
     * @param username 사용자 아이디
     * @return 사용자가 입장한 이후의 채팅방 메시지 목록
     **/
    public List<ChatMessageResponse.ChatMessageInfoDTO> findMessagesAfterMemberJoinedByChatRoomIdAndUsername(Long chatRoomId, String username) {

        boolean isChatRoomExists = chatRoomRepository.existsById(chatRoomId);

        if (!isChatRoomExists) {
            throw new ChatRoomNotFoundException("입장하려는 채팅방을 찾을 수 없습니다.");
        }

        Boolean isMemberChatRoomExists = memberChatRoomRepository.existsByChatRoomIdAndMemberUsername(chatRoomId, username);
        if (!isMemberChatRoomExists) {
            throw new ChatRoomAccessDeniedException("해당 채팅방에 입장하지 않은 사용자입니다.");
        }

        // 채팅방 메시지 조회 로직
        List<ChatMessageResponse.ChatMessageInfoDTO> messages = chatMessageRepository.findMessagesAfterMemberJoinedByChatRoomIdAndUsername(chatRoomId, username);

        if (messages.isEmpty()) {
            throw new ChatMessageRetrievalException("서버 문제로 메시지를 조회하지 못하였습니다. 관리자에게 문의하세요");
        }

        return messages.stream()
                .map(message -> {
                    ChatMessageResponse.ChatMessageInfoDTO.ChatMessageInfoDTOBuilder builder =
                            ChatMessageResponse.ChatMessageInfoDTO.builder()
                            .content(message.getContent())
                            .messageType(message.getMessageType());

                    if (message.getMessageType() == MessageType.TALK) {
                        builder.nickname(message.getNickname())
                                .profileImageS3SavedURL(message.getProfileImageS3SavedURL())
                                .rank(message.getRank())
                                .sentAt(message.getSentAt());
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());
    }
}
