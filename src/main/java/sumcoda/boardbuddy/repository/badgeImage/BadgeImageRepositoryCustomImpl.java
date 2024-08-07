package sumcoda.boardbuddy.repository.badgeImage;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.BadgeImageResponse;

import java.util.List;

import static sumcoda.boardbuddy.entity.QBadgeImage.badgeImage;
import static sumcoda.boardbuddy.entity.QMember.member;

@RequiredArgsConstructor
public class BadgeImageRepositoryCustomImpl implements BadgeImageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BadgeImageResponse.BadgeImageInfosDTO> findBadgeImagesByNickname(String nickname) {
        return jpaQueryFactory.select(Projections.fields(BadgeImageResponse.BadgeImageInfosDTO.class,
                        badgeImage.badgeImageS3SavedURL,
                        badgeImage.badgeYearMonth))
                .from(badgeImage)
                .leftJoin(badgeImage.member, member)
                .where(member.nickname.eq(nickname))
                .orderBy(badgeImage.id.desc())
                .fetch();
    }
}
