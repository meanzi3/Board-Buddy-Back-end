package sumcoda.boardbuddy.repository.chatMessage;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.ChatMessageResponse;
import sumcoda.boardbuddy.exception.MemberChatRoomRetrievalException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static sumcoda.boardbuddy.entity.QChatMessage.chatMessage;
import static sumcoda.boardbuddy.entity.QMember.member;
import static sumcoda.boardbuddy.entity.QMemberChatRoom.*;
import static sumcoda.boardbuddy.entity.QProfileImage.*;
import static sumcoda.boardbuddy.util.ChatMessageUtil.CHAT_MESSAGE_PAGE_SIZE;

@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 사용자가 채팅방에 입장한 이후 최신 N개의 메시지를 조회(V2)
     *
     * @param chatRoomId 조회할 채팅방 ID
     * @param username 조회할 사용자 이름
     * @return 사용자가 입장한 이후 최신 메시지 목록을 반환
     * @since 1.0
     * @version 2.0
     */
    @Override
    public List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> findInitialMessagesByChatRoomIdAndUsernameAndJoinedAt(Long chatRoomId, String username, LocalDateTime joinedAt) {

        // 1) 최신 N+1개 조회 (DESC)
        return jpaQueryFactory
                .select(Projections.fields(
                        ChatMessageResponse.ChatMessageItemInfoProjectionDTO.class,
                        chatMessage.id,
                        chatMessage.content,
                        member.nickname,
                        profileImage.profileImageS3SavedURL.as("profileImageURL"),
                        member.rank,
                        chatMessage.messageType,
                        chatMessage.createdAt.as("sentAt")
                ))
                .from(chatMessage)
                .innerJoin(chatMessage.member, member)
                .leftJoin(member.profileImage, profileImage)
                .where(
                        chatMessage.chatRoom.id.eq(chatRoomId),
                        chatMessage.createdAt.after(joinedAt)
                )
                .orderBy(chatMessage.createdAt.desc(), chatMessage.id.desc())
                .limit(CHAT_MESSAGE_PAGE_SIZE + 1)
                .fetch();
    }

    /**
     * 사용자가 채팅방에서 특정 시점(cursorSentAt, cursorId) 이후의 새로운 N개 메시지를 조회(V2)
     *
     * @param chatRoomId 조회할 채팅방 ID
     * @param username 조회할 사용자 이름
     * @param cursorSentAt 기준이 되는 마지막 메시지 전송 시각
     * @param cursorId 기준이 되는 마지막 메시지 ID
     * @return 기준 시점 이후의 새로운 메시지 목록을 반환
     * @since 1.0
     * @version 2.0
     */
    @Override
    public List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> findNewerMessagesByChatRoomIdAndUsernameAndJoinedAtAndCursor(Long chatRoomId, String username, LocalDateTime joinedAt, LocalDateTime cursorSentAt, Long cursorId) {


        // afterCursor: (createdAt > cursorSentAt)
        // OR (createdAt = cursorSentAt AND id > cursorId)
        // 2) afterCursor 조건 & ASC 정렬
        BooleanExpression afterCursor = afterCursorBooleanExpression(cursorSentAt, cursorId);

        return jpaQueryFactory
                .select(Projections.fields(
                        ChatMessageResponse.ChatMessageItemInfoProjectionDTO.class,
                        chatMessage.id,
                        chatMessage.content,
                        member.nickname,
                        profileImage.profileImageS3SavedURL.as("profileImageURL"),
                        member.rank,
                        chatMessage.messageType,
                        chatMessage.createdAt.as("sentAt")
                ))
                .from(chatMessage)
                .innerJoin(chatMessage.member, member)
                .leftJoin(member.profileImage, profileImage)
                .where(
                        chatMessage.chatRoom.id.eq(chatRoomId),
                        chatMessage.createdAt.after(joinedAt),
                        afterCursor
                )
                .orderBy(chatMessage.createdAt.asc(), chatMessage.id.asc())
                .limit(CHAT_MESSAGE_PAGE_SIZE + 1)
                .fetch();

    }

    /**
     * 커서(cursorSentAt, cursorId) 이후의 메시지를 조회하기 위한 조건을 생성
     *
     * @param cursorSentAt 기준 전송 시각
     * @param cursorId 기준 메시지 ID
     * @return cursorSentAt 이후이거나(cursorSentAt == createdAt && id > cursorId)인 메시지 조건
     */
    private static BooleanExpression afterCursorBooleanExpression(LocalDateTime cursorSentAt, Long cursorId) {
        return chatMessage.createdAt.after(cursorSentAt)
                .or(
                        chatMessage.createdAt.eq(cursorSentAt)
                                .and(chatMessage.id.gt(cursorId))
                );
    }


    /**
     * 사용자가 채팅방에서 특정 시점(cursorSentAt, cursorId) 이전의 과거 N개 메시지를 조회(V2)

     * @param chatRoomId 조회할 채팅방 ID
     * @param username 조회할 사용자 이름
     * @param cursorSentAt 기준이 되는 첫 번째 메시지 전송 시각
     * @param cursorId 기준이 되는 첫 번째 메시지 ID
     * @return 기준 시점 이전의 과거 메시지 목록을 반환
     * @since 1.0
     * @verison 2.0
     */
    @Override
    public List<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> findOlderMessagesByChatRoomIdAndUsernameAndJoinedAtAndCursor(Long chatRoomId, String username, LocalDateTime joinedAt, LocalDateTime cursorSentAt, Long cursorId) {


        // beforeCursor: (createdAt < cursorSentAt)
        // OR (createdAt = cursorSentAt AND id < cursorId)
        // 2) beforeCursor 조건 & DESC 정렬
        BooleanExpression beforeCursor = beforeCursorBooleanExpression(cursorSentAt, cursorId);

        return jpaQueryFactory
                .select(Projections.fields(
                        ChatMessageResponse.ChatMessageItemInfoProjectionDTO.class,
                        chatMessage.id,
                        chatMessage.content,
                        member.nickname,
                        profileImage.profileImageS3SavedURL.as("profileImageURL"),
                        member.rank,
                        chatMessage.messageType,
                        chatMessage.createdAt.as("sentAt")
                ))
                .from(chatMessage)
                .innerJoin(chatMessage.member, member)
                .leftJoin(member.profileImage, profileImage)
                .where(
                        chatMessage.chatRoom.id.eq(chatRoomId),
                        chatMessage.createdAt.after(joinedAt),
                        beforeCursor
                )
                .orderBy(chatMessage.createdAt.desc(), chatMessage.id.desc())
                .limit(CHAT_MESSAGE_PAGE_SIZE + 1)
                .fetch();
    }

    /**
     * 커서(cursorSentAt, cursorId) 이전의 메시지를 조회하기 위한 조건을 생성
     *
     * @param cursorSentAt 기준 전송 시각
     * @param cursorId 기준 메시지 ID
     * @return cursorSentAt 이전이거나(cursorSentAt == createdAt && id < cursorId)인 메시지 조건
     */
    private static BooleanExpression beforeCursorBooleanExpression(LocalDateTime cursorSentAt, Long cursorId) {
        return chatMessage.createdAt.before(cursorSentAt)
                .or(
                        chatMessage.createdAt.eq(cursorSentAt)
                                .and(chatMessage.id.lt(cursorId))
                );
    }

    /**
     * 주어진 채팅방 ID와 사용자 이름으로 memberChatRoom 테이블에서 사용자의 입장 시각을 조회
     *
     * @param chatRoomId 조회할 채팅방 ID
     * @param username 조회할 사용자 이름
     * @return 사용자가 채팅방에 입장한 시각
     * @throws MemberChatRoomRetrievalException 조회된 입장 시각이 null인 경우 발생
     */
    public LocalDateTime findJoinedAtByChatRoomIdAndUsername(Long chatRoomId, String username) {
        return jpaQueryFactory
                .select(memberChatRoom.joinedAt)
                .from(memberChatRoom)
                .where(
                        memberChatRoom.chatRoom.id.eq(chatRoomId),
                        memberChatRoom.member.username.eq(username)
                )
                .fetchOne();
    }


    //    /**
//     * 사용자가 채팅방에 입장한 이후의 전체 메시지 조회(v1)
//     *
//     * @param chatRoomId 채팅방 Id
//     * @param username   사용자 아이디
//     * @return 사용자가 입장한 이후의 채팅방 메시지 목록
//     * @version 1.0
//     **/
//    @Override
//    public List<ChatMessageResponse.ChatMessageItemInfoDTO> findLatestMessagesByChatRoomIdAndUsername(Long chatRoomId, String username) {
//
//        LocalDateTime joinedAt = jpaQueryFactory
//                .select(memberChatRoom.joinedAt)
//                .from(memberChatRoom)
//                .where(
//                        memberChatRoom.chatRoom.id.eq(chatRoomId),
//                        memberChatRoom.member.username.eq(username)
//                )
//                .fetchOne();
//
//        isJoinedAtExists(joinedAt);
//
//        return jpaQueryFactory.select(Projections.fields(ChatMessageResponse.ChatMessageItemInfoDTO.class,
//                        chatMessage.content,
//                        member.nickname,
//                        profileImage.profileImageS3SavedURL,
//                        member.rank,
//                        chatMessage.messageType,
//                        chatMessage.createdAt.as("sentAt")))
//                .from(chatMessage)
//                .innerJoin(chatMessage.member, member)
//                .leftJoin(member.profileImage, profileImage)
//                .where(
//                        chatMessage.chatRoom.id.eq(chatRoomId),
//                        chatMessage.createdAt.after(joinedAt)
//                )
//                .orderBy(
//                        chatMessage.createdAt.asc(),
//                        chatMessage.id.asc()
//                )
//                .fetch();
//    }

    /**
     * 특정 메시지 ID에 해당하는 대화 메시지 조회
     *
     * @param chatMessageId 채팅 메시지 ID
     * @return 특정 메시지 ID에 해당하는 대화 메시지 정보
     **/
    @Override
    public Optional<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> findTalkMessageById(Long chatMessageId) {
        return Optional.ofNullable(jpaQueryFactory.select(Projections.fields(ChatMessageResponse.ChatMessageItemInfoProjectionDTO.class,
                        chatMessage.id,
                        chatMessage.content,
                        member.nickname,
                        profileImage.profileImageS3SavedURL.as("profileImageURL"),
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
    public Optional<ChatMessageResponse.ChatMessageItemInfoProjectionDTO> findEnterOrExitMessageById(Long chatMessageId) {
        return Optional.ofNullable(jpaQueryFactory.select(Projections.fields(ChatMessageResponse.ChatMessageItemInfoProjectionDTO.class,
                        chatMessage.id,
                        chatMessage.content,
                        chatMessage.messageType))
                .from(chatMessage)
                .where(chatMessage.id.eq(chatMessageId))
                .fetchOne());
    }
}

