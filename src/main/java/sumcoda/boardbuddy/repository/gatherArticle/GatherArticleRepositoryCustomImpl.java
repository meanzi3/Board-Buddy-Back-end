package sumcoda.boardbuddy.repository.gatherArticle;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.enumerate.GatherArticleRole;

import java.time.LocalDateTime;
import java.util.List;

import static sumcoda.boardbuddy.entity.QGatherArticle.gatherArticle;
import static sumcoda.boardbuddy.entity.QMember.member;
import static sumcoda.boardbuddy.entity.QMemberGatherArticle.memberGatherArticle;

@Slf4j
@RequiredArgsConstructor
public class GatherArticleRepositoryCustomImpl implements sumcoda.boardbuddy.repository.gatherArticle.GatherArticleRepositoryCustom {

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
                        gatherArticle.status))
                .from(member)
                .join(member.memberGatherArticles, memberGatherArticle)
                .join(memberGatherArticle.gatherArticle, gatherArticle)
                .where(member.username.eq(username)
                        .and(memberGatherArticle.gatherArticleRole.eq(GatherArticleRole.AUTHOR)))
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
                        gatherArticle.status))
                .from(member)
                .join(member.memberGatherArticles, memberGatherArticle)
                .join(memberGatherArticle.gatherArticle, gatherArticle)
                .where(member.username.eq(username)
                        .and(memberGatherArticle.gatherArticleRole.eq(GatherArticleRole.PARTICIPANT))
                        .and(memberGatherArticle.isPermit.eq(true)))
                .fetch();
    }

    // 지난 달에 쓴 모집글 갯수 세기
    @Override
    public long countGatherArticlesByMember(Member member, LocalDateTime startOfLastMonth, LocalDateTime endOfLastMonth) {
        return jpaQueryFactory.select(memberGatherArticle.count())
                .from(memberGatherArticle)
                .where(memberGatherArticle.member.eq(member)
                        .and(memberGatherArticle.gatherArticleRole.eq(GatherArticleRole.AUTHOR))
                        .and(memberGatherArticle.gatherArticle.createdAt.between(startOfLastMonth, endOfLastMonth)))
                .fetchOne();
    }
}
