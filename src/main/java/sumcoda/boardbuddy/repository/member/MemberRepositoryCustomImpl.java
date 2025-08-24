package sumcoda.boardbuddy.repository.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.AuthResponse;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.dto.fetch.MemberDetailProjection;
import sumcoda.boardbuddy.dto.fetch.MemberSummaryProjection;
import sumcoda.boardbuddy.dto.fetch.MemberRankingProjection;
import sumcoda.boardbuddy.entity.Member;

import java.util.List;
import java.util.Optional;

import static sumcoda.boardbuddy.entity.QMember.*;
import static sumcoda.boardbuddy.entity.QProfileImage.*;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<AuthResponse.ProfileDTO> findAuthDTOByUsername(String username) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(AuthResponse.ProfileDTO.class,
                        member.username,
                        member.password,
                        member.role
                ))
                .from(member)
                .where(member.username.eq(username))
                .fetchOne());
    }

    /**
     * @apiNote 현재는 사용률 저조로 기존 메서드 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가시 다시 활성화될 수 있음
     */
//    @Override
//    public Optional<MemberResponse.MemberSummaryDTO> findMemberDTOByUsername(String username) {
//        return Optional.ofNullable(jpaQueryFactory
//                .select(Projections.fields(MemberResponse.MemberSummaryDTO.class,
//                        member.nickname,
//                        member.sido,
//                        member.sgg,
//                        member.emd,
//                        member.phoneNumber,
//                        member.memberType,
//                        profileImage.profileImageS3SavedURL
//                ))
//                .from(member)
//                .leftJoin(member.profileImage, profileImage)
//                .where(member.username.eq(username))
//                .fetchOne());
//    }

    /**
     * @apiNote 임시로 활성화된 메서드
     *          앱 사용률 증가시 비활성화후에 기존 기능 활성화 예정
     */
    @Override
    public Optional<MemberSummaryProjection> findMemberSummaryByUsername(String username) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(MemberSummaryProjection.class,
                        member.nickname,
                        member.phoneNumber,
                        member.memberType,
                        profileImage.s3SavedObjectName
                ))
                .from(member)
                .leftJoin(member.profileImage, profileImage)
                .where(member.username.eq(username))
                .fetchOne());
    }

    @Override
    public List<MemberRankingProjection> findTop3RankingMembers() {
        return jpaQueryFactory
                .select(Projections.constructor(MemberRankingProjection.class,
                        member.nickname,
                        profileImage.s3SavedObjectName
                ))
                .from(member)
                .leftJoin(member.profileImage, profileImage)
                .where(member.rank.isNotNull())
                .orderBy(member.rank.asc())
                .limit(3)
                .fetch();
    }

    // 점수로 정렬
    @Override
    public List<Member> findAllOrderedByRankScore() {
        return jpaQueryFactory.selectFrom(member)
                .orderBy(member.rankScore.desc())
                .fetch();
    }

    @Override
    public Optional<MemberDetailProjection> findMemberDetailByNickname(String nickname) {
        return Optional.ofNullable(jpaQueryFactory
                        .select(Projections.constructor(MemberDetailProjection.class,
                                profileImage.s3SavedObjectName,
                                member.description,
                                member.rank,
                                member.buddyScore,
                                member.joinCount,
                                member.totalExcellentCount,
                                member.totalGoodCount,
                                member.totalBadCount))
                        .from(member)
                        .leftJoin(member.profileImage, profileImage)
                        .where(member.nickname.eq(nickname))
                        .fetchOne());
    }

    /**
     * @apiNote 현재는 사용률 저조로 메서드가 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가시 다시 활성화될 수 있음
     */
//    @Override
//    public Optional<MemberResponse.LocationWithRadiusDTO> findLocationWithRadiusDTOByUsername(String username) {
//        return Optional.ofNullable(jpaQueryFactory
//                .select(Projections.fields(MemberResponse.LocationWithRadiusDTO.class,
//                        member.sido,
//                        member.sgg,
//                        member.emd,
//                        member.radius
//                ))
//                .from(member)
//                .where(member.username.eq(username))
//                .fetchOne());
//    }

    @Override
    public Optional<MemberResponse.UsernameDTO> findUserNameDTOByUsername(String username) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(MemberResponse.UsernameDTO.class,
                        member.username))
                .from(member)
                .where(member.username.eq(username))
                .fetchOne());
    }

    @Override
    public Optional<MemberResponse.IdDTO> findIdDTOByUsername(String username) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(MemberResponse.IdDTO.class,
                        member.id))
                .from(member)
                .where(member.username.eq(username))
                .fetchOne());
    }

    @Override
    public Optional<MemberResponse.UsernameDTO> findUsernameDTOByNickname(String nickname) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(MemberResponse.UsernameDTO.class,
                        member.username))
                .from(member)
                .where(member.nickname.eq(nickname))
                .fetchOne());
    }

    @Override
    public Optional<MemberResponse.NicknameDTO> findNicknameDTOByUsername(String username) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(MemberResponse.NicknameDTO.class,
                        member.nickname))
                .from(member)
                .where(member.username.eq(username))
                .fetchOne());
    }

    /**
     * @apiNote 현재는 사용률 저조로 기능이 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가 시 다시 활성화될 수 있음
     */
//    @Override
//    public List<String> findUsernamesWithGatherArticleInRange(String username, String sido, String sgg, String emd) {
//        return jpaQueryFactory
//                .select(member.username)
//                .from(member)
//                .where(
//                        member.username.ne(username)
//                                .and(
//                                        JPAExpressions
//                                                .selectOne()
//                                                .from(publicDistrict)
//                                                .join(nearPublicDistrict).on(publicDistrict.id.eq(nearPublicDistrict.publicDistrict.id))
//                                                .where(
//                                                        nearPublicDistrict.sido.eq(sido)
//                                                                .and(nearPublicDistrict.sgg.eq(sgg))
//                                                                .and(nearPublicDistrict.emd.eq(emd))
//                                                                .and(member.radius.goe(nearPublicDistrict.radius)) // 반경 조건
//                                                )
//                                                .exists()
//                                )
//                )
//                .fetch();
//    }

}
