package sumcoda.boardbuddy.repository.gatherArticle;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import sumcoda.boardbuddy.dto.GatherArticleResponse;
import sumcoda.boardbuddy.dto.fetch.GatherArticleAuthorProjection;
import sumcoda.boardbuddy.dto.fetch.GatherArticleDetailedInfoProjection;
import sumcoda.boardbuddy.entity.QMember;
import sumcoda.boardbuddy.entity.QMemberGatherArticle;
import sumcoda.boardbuddy.entity.QParticipationApplication;
import sumcoda.boardbuddy.enumerate.MemberGatherArticleRole;
import sumcoda.boardbuddy.entity.Member;
import sumcoda.boardbuddy.enumerate.GatherArticleStatus;
import sumcoda.boardbuddy.enumerate.ParticipationApplicationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static sumcoda.boardbuddy.entity.QGatherArticle.gatherArticle;
import static sumcoda.boardbuddy.entity.QMember.member;
import static sumcoda.boardbuddy.entity.QMemberGatherArticle.memberGatherArticle;
import static sumcoda.boardbuddy.entity.QParticipationApplication.participationApplication;
import static sumcoda.boardbuddy.entity.QProfileImage.profileImage;

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
                        gatherArticle.gatherArticleStatus.as("status")
                ))
                .from(member)
                .join(member.memberGatherArticles, memberGatherArticle)
                .join(memberGatherArticle.gatherArticle, gatherArticle)
                .where(member.username.eq(username)
                        .and(memberGatherArticle.memberGatherArticleRole.eq(MemberGatherArticleRole.AUTHOR)))
                .fetch();
    }

    @Override
    public List<GatherArticleResponse.MyParticipationInfosDTO> findParticipationsByUsername(String username) {

        QMember author = new QMember("author");
        QMemberGatherArticle authorMemberGatherArticle = new QMemberGatherArticle("authorMemberGatherArticle");

        return jpaQueryFactory.select(Projections.fields(GatherArticleResponse.MyParticipationInfosDTO.class,
                        gatherArticle.id,
                        gatherArticle.title,
                        gatherArticle.description,
                        Projections.fields(GatherArticleResponse.AuthorSimpleDTO.class,
                                author.nickname.as("nickname"),
                                author.rank.as("rank")).as("author"),
                        gatherArticle.meetingLocation,
                        gatherArticle.maxParticipants,
                        gatherArticle.currentParticipants,
                        gatherArticle.startDateTime,
                        gatherArticle.endDateTime,
                        gatherArticle.createdAt,
                        gatherArticle.gatherArticleStatus.as("status")
                ))
                .from(member)
                .join(member.memberGatherArticles, memberGatherArticle)
                .join(memberGatherArticle.gatherArticle, gatherArticle)
                .join(gatherArticle.memberGatherArticles, authorMemberGatherArticle)
                .join(authorMemberGatherArticle.member, author)
                .where(member.username.eq(username)
                        .and(memberGatherArticle.memberGatherArticleRole.eq(MemberGatherArticleRole.PARTICIPANT))
                        .and(authorMemberGatherArticle.memberGatherArticleRole.eq(MemberGatherArticleRole.AUTHOR)))
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

    // 지난 달에 쓴 모집글 갯수 세기
    @Override
    public long countGatherArticlesByMember(Member member, LocalDateTime startOfLastMonth, LocalDateTime endOfLastMonth) {
        return jpaQueryFactory.select(memberGatherArticle.count())
                .from(memberGatherArticle)
                .where(memberGatherArticle.member.eq(member)
                        .and(memberGatherArticle.memberGatherArticleRole.eq(MemberGatherArticleRole.AUTHOR))
                        .and(memberGatherArticle.gatherArticle.createdAt.between(startOfLastMonth, endOfLastMonth)))
                .fetchOne();
    }

    @Override
    public Optional<GatherArticleResponse.IdDTO> findIdDTOById(Long gatherArticleId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(GatherArticleResponse.IdDTO.class,
                        gatherArticle.id))
                .from(gatherArticle)
                .where(gatherArticle.id.eq(gatherArticleId))
                .fetchOne());
    }

    /**
     * @apiNote V1 - 내 동네 반경 n km 기반 모집글 리스트 조회 (현재 미사용, 향후 복원 가능)
     */
//    @Override
//    public Slice<GatherArticleResponse.ReadSliceDTO> findReadSliceDTOByLocationAndStatusAndSort(
//            List<String> sidoList, List<String> sggList, List<String> emdList,
//            String status, String sort, MemberGatherArticleRole role, Pageable pageable) {
//
//        List<GatherArticleResponse.ReadSliceDTO> results = jpaQueryFactory.select(Projections.fields(
//                        GatherArticleResponse.ReadSliceDTO.class,
//                        gatherArticle.id,
//                        gatherArticle.title,
//                        gatherArticle.description,
//                        Projections.fields(GatherArticleResponse.AuthorSimpleDTO.class,
//                                member.nickname.as("nickname"),
//                                member.rank.as("rank")).as("author"),
//                        gatherArticle.meetingLocation,
//                        gatherArticle.maxParticipants,
//                        gatherArticle.currentParticipants,
//                        gatherArticle.startDateTime,
//                        gatherArticle.endDateTime,
//                        gatherArticle.createdAt,
//                        gatherArticle.gatherArticleStatus.as("status")))
//                .from(gatherArticle)
//                .join(gatherArticle.memberGatherArticles, memberGatherArticle)
//                .join(memberGatherArticle.member, member)
//                .where(
//                        inLocation(sidoList, sggList, emdList),
//                        eqStatus(status),
//                        eqMemberGatherArticleRole(role)
//                )
//                .orderBy(getOrderSpecifiers(sort))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize() + 1)
//                .fetch();
//
//        boolean hasNext = false;
//
//        if (results.size() > pageable.getPageSize()) {
//            results.remove(pageable.getPageSize());
//            hasNext = true;
//        }
//
//        return new SliceImpl<>(results, pageable, hasNext);
//    }

    /**
     * @apiNote V2 - 사용자가 지정한 지역 기반 모집글 리스트 조회
     */
    @Override
    public Slice<GatherArticleResponse.ReadSliceDTO> findReadSliceDTOByLocationV2AndStatusAndSortAndKeyword(
            String sido, String sgg,
            String status, String sort, String keyword, MemberGatherArticleRole role, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if (sido != null) {
            builder.and(gatherArticle.sido.eq(sido));
        }

        if (sgg != null) {
            builder.and(gatherArticle.sgg.eq(sgg));
        }

        BooleanExpression keywordCondition = containsKeywordInTitleOrDescription(keyword);
        if (keywordCondition != null) {
            builder.and(keywordCondition);
        }

        List<GatherArticleResponse.ReadSliceDTO> results = jpaQueryFactory
                .select(Projections.fields(
                        GatherArticleResponse.ReadSliceDTO.class,
                        gatherArticle.id,
                        gatherArticle.title,
                        gatherArticle.description,
                        Projections.fields(GatherArticleResponse.AuthorSimpleDTO.class,
                                member.nickname.as("nickname"),
                                member.rank.as("rank")).as("author"),
                        gatherArticle.meetingLocation,
                        gatherArticle.maxParticipants,
                        gatherArticle.currentParticipants,
                        gatherArticle.startDateTime,
                        gatherArticle.endDateTime,
                        gatherArticle.createdAt,
                        gatherArticle.gatherArticleStatus.as("status")))
                .from(gatherArticle)
                .join(gatherArticle.memberGatherArticles, memberGatherArticle)
                .join(memberGatherArticle.member, member)
                .where(
                        builder,
                        eqStatus(status),
                        eqMemberGatherArticleRole(role)
                )
                .orderBy(getOrderSpecifiers(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            results.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    /**
     * 특정 모집글 Id로 간단한 모집글 정보 조회
     *
     * @param gatherArticleId 모집글 Id
     * @return 모집글 정보가 포함된 SummaryInfoDTO 객체
     **/
    @Override
    public Optional<GatherArticleResponse.SummaryInfoDTO> findSimpleInfoByGatherArticleId(Long gatherArticleId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(GatherArticleResponse.SummaryInfoDTO.class,
                        gatherArticle.title,
                        gatherArticle.meetingLocation,
                        gatherArticle.maxParticipants,
                        gatherArticle.currentParticipants,
                        gatherArticle.startDateTime,
                        gatherArticle.endDateTime
                ))
                .from(gatherArticle)
                .where(gatherArticle.id.eq(gatherArticleId))
                .fetchOne());
    }

    private BooleanExpression inLocation(List<String> sidoList, List<String> sggList, List<String> emdList) {
        return gatherArticle.sido.in(sidoList)
                .and(gatherArticle.sgg.in(sggList))
                .and(gatherArticle.emd.in(emdList));
    }

    private BooleanExpression eqStatus(String status) {
        return status != null ? gatherArticle.gatherArticleStatus.eq(GatherArticleStatus.valueOf(status.toUpperCase())) : null;
    }

    private BooleanExpression eqMemberGatherArticleRole(MemberGatherArticleRole role) {
        return memberGatherArticle.memberGatherArticleRole.eq(role);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(String sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (GatherArticleStatus.SOON.getValue().equals(sort)) {
            orderSpecifiers.add(gatherArticle.startDateTime.asc());
        }

        orderSpecifiers.add(gatherArticle.id.desc());

        return orderSpecifiers.toArray(new OrderSpecifier<?>[0]);
    }

    /**
     * 특정 모집글의 상세 정보를 조회
     *
     * @param gatherArticleId 조회할 모집글의 ID
     * @return 모집글 상세 정보를 담은 GatherArticleDetailedInfoDTO 객체
     */
    @Override
    public Optional<GatherArticleDetailedInfoProjection> findGatherArticleDetailedInfoByGatherArticleId(Long gatherArticleId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(GatherArticleDetailedInfoProjection.class,
                        gatherArticle.title,
                        gatherArticle.description,
                        gatherArticle.sido,
                        gatherArticle.sgg,
                        gatherArticle.emd,
                        gatherArticle.meetingLocation,
                        gatherArticle.x,
                        gatherArticle.y,
                        gatherArticle.maxParticipants,
                        gatherArticle.currentParticipants,
                        gatherArticle.startDateTime,
                        gatherArticle.endDateTime,
                        gatherArticle.createdAt,
                        gatherArticle.gatherArticleStatus
                ))
                .from(gatherArticle)
                .where(gatherArticle.id.eq(gatherArticleId))
                .fetchOne());
    }

    @Override
    public Optional<GatherArticleAuthorProjection> findGatherArticleAuthorByGatherArticleId(Long gatherArticleId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(GatherArticleAuthorProjection.class,
                        member.nickname,
                        member.rank,
                        profileImage.s3SavedObjectName,
                        member.description
                ))
                .from(gatherArticle)
                // 모집글 작성자는 반드시 있으므로 inner join
                .innerJoin(gatherArticle.memberGatherArticles, memberGatherArticle)
                .innerJoin(memberGatherArticle.member, member)
                // 프로필 이미지는 선택적이므로 left join
                .leftJoin(member.profileImage, profileImage)
                .where(
                        gatherArticle.id.eq(gatherArticleId),
                        memberGatherArticle.memberGatherArticleRole.eq(MemberGatherArticleRole.AUTHOR)
                )
                .fetchOne());
    }

    /**
     * 특정 모집글에 대한 사용자의 참여 신청 상태를 조회
     *
     * @param gatherArticleId 조회할 모집글의 ID
     * @param username 조회할 사용자의 username
     * @return 참여 신청 상태를 담은 ParticipationApplicationStatusDTO 객체
     */
    @Override
    public Optional<GatherArticleResponse.ParticipationApplicationStatusDTO> findParticipationApplicationStatusDTOByGatherArticleIdAndUsername(Long gatherArticleId, String username) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(GatherArticleResponse.ParticipationApplicationStatusDTO.class,
                        participationApplicationStatusExpression(participationApplication)
                ))
                .from(gatherArticle)
                // 모집글은 무조건 조회되어야 하고 참가 신청 기록이 없으면 NULL(NONE)으로 조회
                .leftJoin(gatherArticle.memberGatherArticles, memberGatherArticle)
                // username에 해당하는 memberGatherArticle 레코드만 조회
                .on(memberGatherArticle.member.username.eq(username))
                .leftJoin(memberGatherArticle.member, member)
                .leftJoin(memberGatherArticle.participationApplication, participationApplication)
                .where(gatherArticle.id.eq(gatherArticleId))
                .fetchOne());
    }

    /**
     * participationApplication이 없으면 NONE, 있으면 실제 상태를 반환하는 표현식을 반환
     *
     * @param participationApplication QParticipationApplication 타입의 참가 신청 엔티티
     * @return ParticipationApplicationStatus를 계산하는 Expression
     */
    public static Expression<ParticipationApplicationStatus> participationApplicationStatusExpression(
            QParticipationApplication participationApplication) {
        return new CaseBuilder()
                .when(participationApplication.id.isNull()).then(ParticipationApplicationStatus.NONE)
                .otherwise(participationApplication.participationApplicationStatus)
                .as("participationApplicationStatus");
    }

    @Override
    public Optional<GatherArticleResponse.TitleDTO> findTitleDTOById(Long gatherArticleId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(GatherArticleResponse.TitleDTO.class,
                        gatherArticle.title))
                .from(gatherArticle)
                .where(gatherArticle.id.eq(gatherArticleId))
                .fetchOne());
    }

    /**
     * @apiNote 임시 비활성화된 상태
     *          위치 관련 코드 제거 필요
     */
//    @Override
//    public Optional<GatherArticleResponse.LocationInfoDTO> findLocationInfoDTOById(Long gatherArticleId) {
//        return Optional.ofNullable(jpaQueryFactory
//                .select(Projections.fields(GatherArticleResponse.LocationInfoDTO.class,
//                        gatherArticle.sido,
//                        gatherArticle.sgg,
//                        gatherArticle.emd
//                ))
//                .from(gatherArticle)
//                .where(gatherArticle.id.eq(gatherArticleId))
//                .fetchOne());
//    }

    /**
     * @apiNote 임시 비활성화된 상태
     *          위치 관련 코드 제거 필요
     */
//    @Override
//    public List<GatherArticleResponse.SearchResultDTO> findSearchResultDTOByKeyword(List<String> sidoList, List<String> sggList, List<String> emdList, MemberGatherArticleRole role, String keyword) {
//
//        return jpaQueryFactory
//                .select(Projections.fields(
//                        GatherArticleResponse.SearchResultDTO.class,
//                        gatherArticle.id,
//                        gatherArticle.title,
//                        gatherArticle.description,
//                        Projections.fields(GatherArticleResponse.AuthorSimpleDTO.class,
//                                member.nickname.as("nickname"),
//                                member.rank.as("rank")).as("author"),
//                        gatherArticle.meetingLocation,
//                        gatherArticle.maxParticipants,
//                        gatherArticle.currentParticipants,
//                        gatherArticle.startDateTime,
//                        gatherArticle.endDateTime,
//                        gatherArticle.createdAt,
//                        gatherArticle.gatherArticleStatus.as("status")))
//                .from(gatherArticle)
//                .join(gatherArticle.memberGatherArticles, memberGatherArticle)
//                .join(memberGatherArticle.member, member)
//                .where(
//                        inLocation(sidoList, sggList, emdList),
//                        eqMemberGatherArticleRole(role),
//                        titleOrDescriptionContains(keyword)
//                )
//                .orderBy(gatherArticle.id.desc())
//                .fetch();
//
//    }

    private BooleanExpression containsKeywordInTitleOrDescription(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        // 키워드 공백 제거
        String keywordWithoutWhiteSpace = keyword.replaceAll("\\s", "");

        // SQL 함수를 사용하여 공백 제거
        StringTemplate titleTemplate = Expressions.stringTemplate("replace({0}, ' ', '')", gatherArticle.title);
        StringTemplate descriptionTemplate = Expressions.stringTemplate("replace({0}, ' ', '')", gatherArticle.description);

        return titleTemplate.containsIgnoreCase(keywordWithoutWhiteSpace)
                .or(descriptionTemplate.containsIgnoreCase(keywordWithoutWhiteSpace));
    }

    @Override
    public Optional<GatherArticleResponse.StatusDTO> findStatusDTOById(Long gatherArticleId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(GatherArticleResponse.StatusDTO.class,
                        gatherArticle.gatherArticleStatus.as("status")))
                .from(gatherArticle)
                .where(gatherArticle.id.eq(gatherArticleId))
                .fetchOne());
    }

    @Override
    public List<Long> findGatherArticleIdsByUsername(String username) {
        // Author 역할인 GatherArticle ID 목록 조회
        return jpaQueryFactory
                .select(memberGatherArticle.gatherArticle.id)
                .from(memberGatherArticle)
                .where(
                        memberGatherArticle.member.username.eq(username)
                                .and(memberGatherArticle.memberGatherArticleRole.eq(MemberGatherArticleRole.AUTHOR))
                )
                .fetch();
    }
}
