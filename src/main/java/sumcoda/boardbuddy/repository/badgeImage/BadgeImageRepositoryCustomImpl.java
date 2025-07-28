package sumcoda.boardbuddy.repository.badgeImage;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.fetch.BadgeImageInfoProjection;

import java.util.List;

import static sumcoda.boardbuddy.entity.QBadgeImage.badgeImage;
import static sumcoda.boardbuddy.entity.QMember.member;

@RequiredArgsConstructor
public class BadgeImageRepositoryCustomImpl implements BadgeImageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BadgeImageInfoProjection> findBadgeImagesByNickname(String nickname) {
        return jpaQueryFactory
                .select(Projections.constructor(BadgeImageInfoProjection.class,
                        badgeImage.s3SavedObjectName,
                        badgeImage.badgeYearMonth))
                .from(badgeImage)
                .leftJoin(badgeImage.member, member)
                .where(member.nickname.eq(nickname))
                .orderBy(badgeImage.id.desc())
                .fetch();
    }

    @Override
    public List<BadgeImageInfoProjection> findTop3BadgeImagesByNickname(String nickname) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(BadgeImageInfoProjection.class,
                                badgeImage.s3SavedObjectName,
                                badgeImage.badgeYearMonth))
                .from(badgeImage)
                .leftJoin(badgeImage.member, member)
                .where(member.nickname.eq(nickname))
                .orderBy(badgeImage.id.desc())
                .limit(3)
                .fetch();
    }
}
