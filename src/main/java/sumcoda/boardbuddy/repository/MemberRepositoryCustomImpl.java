package sumcoda.boardbuddy.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.AuthResponse;
import sumcoda.boardbuddy.dto.MemberResponse;
import sumcoda.boardbuddy.entity.Member;

import java.util.List;
import java.util.Optional;

import static sumcoda.boardbuddy.entity.QBadgeImage.badgeImage;
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

    @Override
    public Optional<MemberResponse.ProfileDTO> findMemberDTOByUsername(String username) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(MemberResponse.ProfileDTO.class,
                        member.nickname,
                        member.sido,
                        member.sigu,
                        member.dong,
                        member.phoneNumber,
                        profileImage.profileImageS3SavedURL
                ))
                .from(member)
                .leftJoin(member.profileImage, profileImage)
                .where(member.username.eq(username))
                .fetchOne());
    }

    @Override
    public List<MemberResponse.RankingsDTO> findTop3RankingMembers() {
        return jpaQueryFactory
                .select(Projections.fields(MemberResponse.RankingsDTO.class,
                        member.nickname,
                        profileImage.profileImageS3SavedURL
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
    public Optional<MemberResponse.ProfileInfosDTO> findMemberProfileByNickname(String nickname) {
        List<String> badges = jpaQueryFactory.select(badgeImage.badgeImageS3SavedURL)
                .from(badgeImage)
                .where(badgeImage.member.nickname.eq(nickname))
                .orderBy(badgeImage.id.desc())
                .limit(3)
                .fetch();

        return Optional.ofNullable(jpaQueryFactory
                        .select(Projections.fields(MemberResponse.ProfileInfosDTO.class,
                                member.description,
                                member.rank,
                                member.buddyScore,
                                member.joinCount,
                                member.totalExcellentCount,
                                member.totalGoodCount,
                                member.totalBadCount))
                        .from(member)
                        .where(member.nickname.eq(nickname))
                        .fetchOne())
                .map(profileInfosDTO -> profileInfosDTO.toBuilder().badges(badges).build());
    }

    @Override
    public Optional<MemberResponse.LocationWithRadiusDTO> findLocationWithRadiusDTOByUsername(String username) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(MemberResponse.LocationWithRadiusDTO.class,
                        member.sido,
                        member.sigu,
                        member.dong,
                        member.radius
                ))
                .from(member)
                .where(member.username.eq(username))
                .fetchOne());
    }
}
