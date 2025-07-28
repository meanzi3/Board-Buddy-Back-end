package sumcoda.boardbuddy.repository.profileImage;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.fetch.ProfileImageObjectNameProjection;

import java.util.Optional;

import static sumcoda.boardbuddy.entity.QMember.member;
import static sumcoda.boardbuddy.entity.QProfileImage.*;

@RequiredArgsConstructor
public class ProfileImageRepositoryCustomImpl implements ProfileImageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByUsername(String username) {
        return jpaQueryFactory
                .selectOne()
                .from(profileImage)
                .leftJoin(profileImage.member, member)
                .where(member.username.eq(username))
                .fetchFirst()  // 조회 결과가 없으면 null, 있으면 1 리턴
                != null;
    }

    @Override
    public Optional<ProfileImageObjectNameProjection> findProfileImageObjectNameByUsername(String username) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(ProfileImageObjectNameProjection.class,
                        profileImage.s3SavedObjectName))
                .from(profileImage)
                .leftJoin(profileImage.member, member)
                .where(member.username.eq(username))
                .fetchOne());
    }
}
