package sumcoda.boardbuddy.repository.gatherArticle;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.enumerate.MemberGatherArticleRole;

import java.util.List;

import static sumcoda.boardbuddy.entity.QGatherArticle.gatherArticle;
import static sumcoda.boardbuddy.entity.QMember.member;
import static sumcoda.boardbuddy.entity.QMemberGatherArticle.memberGatherArticle;

@Slf4j
@RequiredArgsConstructor
public class GatherArticleRepositoryCustomImpl implements GatherArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GatherArticleResponse.GatherArticleInfosDTO> findGatherArticleInfosByUsername(String username) {
        return jpaQueryFactory.select(Projections.fields(GatherArticleResponse.GatherArticleInfosDTO.class,
                        gatherArticle.id,
                        gatherArticle.title,
                        gatherArticle.description,
                        gatherArticle.meetingLocation,
                        gatherArticle.maxParticipants,
                        gatherArticle.currentParticipants,
                        gatherArticle.startDateTime,
                        gatherArticle.endDateTime,
                        gatherArticle.createdAt,
                        gatherArticle.gatherArticleStatus
                        ))
                .from(member)
                .join(member.memberGatherArticles, memberGatherArticle)
                .join(memberGatherArticle.gatherArticle, gatherArticle)
                .where(member.username.eq(username)
                        .and(memberGatherArticle.memberGatherArticleRole.eq(MemberGatherArticleRole.AUTHOR)))
                .fetch();
    }

    @Override
    public List<GatherArticleResponse.GatherArticleInfosDTO> findParticipationsByUsername(String username) {
        return jpaQueryFactory.select(Projections.fields(GatherArticleResponse.GatherArticleInfosDTO.class,
                        gatherArticle.id,
                        gatherArticle.title,
                        gatherArticle.description,
                        gatherArticle.meetingLocation,
                        gatherArticle.maxParticipants,
                        gatherArticle.currentParticipants,
                        gatherArticle.startDateTime,
                        gatherArticle.endDateTime,
                        gatherArticle.createdAt,
                        gatherArticle.gatherArticleStatus
                ))
                .from(member)
                .join(member.memberGatherArticles, memberGatherArticle)
                .join(memberGatherArticle.gatherArticle, gatherArticle)
                .where(member.username.eq(username)
                        .and(memberGatherArticle.memberGatherArticleRole.eq(MemberGatherArticleRole.PARTICIPANT)))
                .fetch();
    }

    @Override
    public Boolean isMemberAuthorOfGatherArticle(Long gatherArticleId, String username) {
        return jpaQueryFactory
                .selectOne()
                .from(memberGatherArticle)
                .leftJoin(memberGatherArticle.gatherArticle, gatherArticle)
                .leftJoin(memberGatherArticle.member, member)
                .where(gatherArticle.id.eq(gatherArticleId)
                        .and(member.username.eq(username)
                                .and(memberGatherArticle.memberGatherArticleRole.eq(MemberGatherArticleRole.AUTHOR))))
                .fetchOne() != null;
    }
}
