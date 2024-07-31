package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.dto.ChatRoomResponse;
import sumcoda.boardbuddy.dto.MemberChatRoomResponse;
import sumcoda.boardbuddy.entity.ChatRoom;
import sumcoda.boardbuddy.entity.GatherArticle;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.entity.MemberChatRoom;
import sumcoda.boardbuddy.enumerate.MemberChatRoomRole;
import sumcoda.boardbuddy.exception.*;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleNotFoundException;
import sumcoda.boardbuddy.exception.gatherArticle.GatherArticleRetrievalException;
import sumcoda.boardbuddy.exception.member.MemberNotFoundException;
import sumcoda.boardbuddy.exception.member.MemberRetrievalException;
import sumcoda.boardbuddy.repository.chatRoom.ChatRoomRepository;
import sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepository;
import sumcoda.boardbuddy.repository.memberChatRoom.MemberChatRoomRepository;
import sumcoda.boardbuddy.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final MemberChatRoomRepository memberChatRoomRepository;

    private final MemberRepository memberRepository;

    private final GatherArticleRepository gatherArticleRepository;

    /**
     * 채팅방 생성
     *
     * @param gatherArticleId 채팅방과 연관된 모집글 Id
     */
    @Transactional
    public void createChatRoom(Long gatherArticleId) {
        GatherArticle gatherArticle = gatherArticleRepository.findById(gatherArticleId)
                .orElseThrow(() -> new GatherArticleNotFoundException("모집글의 정보를 찾을 수 없습니다."));

        Long gatherArticleValidateId = gatherArticle.getId();

        if (gatherArticleValidateId == null) {
            throw new GatherArticleRetrievalException("서버 문제로 해당 모집글의 정보를 찾을 수 없습니다.");
        }

        ChatRoom chatRoom = ChatRoom.buildChatRoom(gatherArticle);

        Long chatRoomId = chatRoomRepository.save(chatRoom).getId();

        if (chatRoomId == null) {
            throw new ChatRoomSaveException("서버 문제로 채팅방의 정보를 저장하지 못했습니다.");
        }
    }

    /**
     * 사용자가 특정 모집글과 관련된 채팅방에 입장
     *
     * @param gatherArticleId 모집글 Id
     * @param username 사용자 아이디
     **/
    @Transactional
    public Long enterChatRoom(Long gatherArticleId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findByGatherArticleId(gatherArticleId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 모집글에 대한 채팅방이 존재하지 않습니다."));

        Long chatRoomId = chatRoom.getId();

        if (chatRoomId == null) {
            throw new ChatRoomRetrievalException("서버 문제로 해당 채팅방의 정보를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("해당 사용자를 찾을 수 없습니다."));

        Boolean isMemberChatRoomExists = memberChatRoomRepository.existsByGatherArticleIdAndUsername(gatherArticleId, username);
        if (isMemberChatRoomExists) {
            throw new AlreadyEnteredChatRoomException("해당 채팅방은 이미 입장한 채팅방입니다.");
        }

        MemberChatRoom memberChatRoom = MemberChatRoom.buildMemberChatRoom(LocalDateTime.now(), MemberChatRoomRole.PARTICIPANT, member, chatRoom);

        Long memberChatRoomId = memberChatRoomRepository.save(memberChatRoom).getId();

        if (memberChatRoomId == null) {
            throw new MemberChatRoomSaveException("서버 문제로 채팅방 관련 사용자의 정보를 저장하지 못했습니다. 관리자에게 문의하세요.");
        }

        return chatRoomId;
    }

    /**
     * 사용자가 특정 모집글과 관련된 채팅방에서 퇴장
     *
     * @param gatherArticleId 모집글 Id
     * @param username 사용자 아이디
     **/
    @Transactional
    public Long leaveChatRoom(Long gatherArticleId, String username) {
        ChatRoomResponse.ValidateDTO chatRoomValidateDTO = chatRoomRepository.findValidateDTOByGatherArticleId(gatherArticleId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 모집글에 대한 채팅방이 존재하지 않습니다"));

        Long chatRoomId = chatRoomValidateDTO.getId();
        if (chatRoomId == null) {
            throw new ChatRoomRetrievalException("서버문제로 해당 모집글에 대한 채팅방 정보를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        MemberChatRoomResponse.ValidateDTO memberChatRoomValidateDTO = memberChatRoomRepository.findByGatherArticleIdAndUsername(gatherArticleId, username)
                .orElseThrow(() -> new MemberChatRoomNotFoundException("채팅방 관련 사용자의 정보를 찾을 수 없습니다."));

        MemberChatRoomRole memberChatRoomRole = memberChatRoomValidateDTO.getMemberChatRoomRole();
        if (memberChatRoomRole == null) {
            throw new MemberChatRoomRetrievalException("서버 문제로 채팅방 관련 사용자의 정보를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        if (memberChatRoomValidateDTO.getMemberChatRoomRole() == MemberChatRoomRole.HOST) {
            throw new ChatRoomHostCannotLeaveException("채팅방의 방장은 채팅방을 퇴장할 수 없습니다.");
        }

        Long memberChatRoomId = memberChatRoomValidateDTO.getId();

        if (memberChatRoomId == null) {
            throw new MemberChatRoomRetrievalException("서버 문제로 채팅방 관련 사용자의 정보를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        memberChatRoomRepository.deleteById(memberChatRoomId);

        return chatRoomId;
    }

    /**
     * 특정 사용자가 참여하고 있는 채팅방 상세 정보 목록 조회
     *
     * @param username 사용자 아이디
     * @return 사용자가 참여하고 있는 채팅방 상세 정보 목록
     **/
    public List<ChatRoomResponse.ChatRoomDetailsDTO> getChatRoomDetailsListByUsername(String username) {
        Boolean isMemberExists = memberRepository.existsByUsername(username);
        if (!isMemberExists) {
            throw new MemberRetrievalException("서버 문제로 사용자의 정보를 찾을 수 없습니다. 관리자에게 문의하세요.");
        }

        return chatRoomRepository.findChatRoomDetailsListByUsername(username);
    }
}