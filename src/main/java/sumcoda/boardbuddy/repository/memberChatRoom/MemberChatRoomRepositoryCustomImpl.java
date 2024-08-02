package sumcoda.boardbuddy.repository.memberChatRoom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.MemberChatRoomResponse;

import java.util.Optional;

import static sumcoda.boardbuddy.entity.QChatRoom.chatRoom;
import static sumcoda.boardbuddy.entity.QGatherArticle.gatherArticle;
import static sumcoda.boardbuddy.entity.QMember.member;
import static sumcoda.boardbuddy.entity.QMemberChatRoom.*;

@RequiredArgsConstructor
public class MemberChatRoomRepositoryCustomImpl implements MemberChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 특정 모집글 Id와 닉네임을 가진 사용자가 존재하는지 확인
     *
     * @param gatherArticleId 모집글 Id
     * @param username 사용자 아이디
     * @return 사용자가 존재하면 true, 아니면 false
     **/
    @Override
    public Boolean existsByGatherArticleIdAndUsername(Long gatherArticleId, String username) {
        return jpaQueryFactory
                .selectOne()
                .from(memberChatRoom)
                .leftJoin(memberChatRoom.member, member)
                .leftJoin(memberChatRoom.chatRoom, chatRoom)
                .leftJoin(chatRoom.gatherArticle, gatherArticle)
                .where(gatherArticle.id.eq(gatherArticleId)
                        .and(member.username.eq(username)))
                .fetchOne() != null;
    }

    /**
     * 특정 모집글 Id와 사용자 아이디로 MemberChatRoom 정보를 조회
     *
     * @param gatherArticleId 모집글 Id
     * @param username 사용자 아이디
     * @return MemberChatRoom 정보가 포함된 ValidateDTO 객체
     **/
    @Override
    public Optional<MemberChatRoomResponse.ValidateDTO> findByGatherArticleIdAndUsername(Long gatherArticleId, String username) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(MemberChatRoomResponse.ValidateDTO.class,
                        memberChatRoom.id,
                        memberChatRoom.memberChatRoomRole,
                        member.nickname
                ))
                .from(memberChatRoom)
                .leftJoin(memberChatRoom.chatRoom, chatRoom)
                .leftJoin(memberChatRoom.member, member)
                .leftJoin(chatRoom.gatherArticle, gatherArticle)
                .where(gatherArticle.id.eq(gatherArticleId)
                        .and(member.username.eq(username)))
                .fetchOne());
    }
}
