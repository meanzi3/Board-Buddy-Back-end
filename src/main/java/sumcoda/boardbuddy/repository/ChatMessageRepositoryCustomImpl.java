package sumcoda.boardbuddy.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.ChatMessageResponse;
import sumcoda.boardbuddy.exception.MemberChatRoomRetrievalException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static sumcoda.boardbuddy.entity.QChatMessage.chatMessage;
import static sumcoda.boardbuddy.entity.QChatRoom.chatRoom;
import static sumcoda.boardbuddy.entity.QMember.member;
import static sumcoda.boardbuddy.entity.QMemberChatRoom.*;
import static sumcoda.boardbuddy.entity.QProfileImage.*;

@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 사용자가 채팅방에 입장한 이후의 메시지 조회
     *
     * @param chatRoomId 채팅방 Id
     * @param username 사용자 아이디
     * @return 사용자가 입장한 이후의 채팅방 메시지 목록
     **/
    @Override
    public List<ChatMessageResponse.ChatMessageInfoDTO> findMessagesAfterMemberJoinedByChatRoomIdAndUsername(Long chatRoomId, String username) {

        LocalDateTime joinedAt = jpaQueryFactory.select(memberChatRoom.joinedAt)
                .from(memberChatRoom)
                .where(memberChatRoom.chatRoom.id.eq(chatRoomId)
                        .and(memberChatRoom.member.username.eq(username)))
                .fetchOne();

        isJoinedAtExists(joinedAt);

        return jpaQueryFactory.select(Projections.fields(ChatMessageResponse.ChatMessageInfoDTO.class,
                        chatMessage.content,
                        member.nickname,
                        profileImage.profileImageS3SavedURL,
                        member.rank,
                        chatMessage.messageType,
                        chatMessage.createdAt.as("sentAt")))
                .from(chatMessage)
                .leftJoin(chatMessage.member, member)
                .leftJoin(member.profileImage, profileImage)
                .leftJoin(chatMessage.chatRoom, chatRoom)
                .where(chatRoom.id.eq(chatRoomId).and(chatMessage.createdAt.after(joinedAt)))
                .orderBy(chatMessage.createdAt.asc())
                .fetch();
    }

    private static void isJoinedAtExists(LocalDateTime joinedAt) {
        if (joinedAt == null) {
            throw new MemberChatRoomRetrievalException("서버 문제로 사용자가 해당 채팅방에 입장한 시간을 찾을 수 없습니다. 관리자에게 문의하세요.");
        }
    }

    /**
     * 특정 메시지 ID에 해당하는 대화 메시지 조회
     *
     * @param chatMessageId 채팅 메시지 ID
     * @return 특정 메시지 ID에 해당하는 대화 메시지 정보
     **/
    @Override
    public Optional<ChatMessageResponse.ChatMessageInfoDTO> findTalkMessageById(Long chatMessageId) {
        return Optional.ofNullable(jpaQueryFactory.select(Projections.fields(ChatMessageResponse.ChatMessageInfoDTO.class,
                        chatMessage.content,
                        member.nickname,
                        profileImage.profileImageS3SavedURL,
                        member.rank,
                        chatMessage.messageType,
                        chatMessage.createdAt.as("sentAt")))
                .from(chatMessage)
                .leftJoin(chatMessage.member, member)
                .leftJoin(member.profileImage, profileImage)
                .where(chatMessage.id.eq(chatMessageId))
                .fetchOne());
    }

    /**
     * 특정 메시지 ID에 해당하는 입장/퇴장 메시지 조회
     *
     * @param chatMessageId 채팅 메시지 ID
     * @return 특정 메시지 ID에 해당하는 입장/퇴장 메시지 정보
     **/
    @Override
    public Optional<ChatMessageResponse.EnterOrExitMessageInfoDTO> findEnterOrExitMessageById(Long chatMessageId) {
        return Optional.ofNullable(jpaQueryFactory.select(Projections.fields(ChatMessageResponse.EnterOrExitMessageInfoDTO.class,
                        chatMessage.content,
                        chatMessage.messageType))
                .from(chatMessage)
                .where(chatMessage.id.eq(chatMessageId))
                .fetchOne());
    }
}
