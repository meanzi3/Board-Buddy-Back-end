package sumcoda.boardbuddy.repository.badgeImage;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.BadgeImageResponse;

import java.util.List;

import static sumcoda.boardbuddy.entity.QBadgeImage.badgeImage;

@RequiredArgsConstructor
public class BadgeImageRepositoryCustomImpl implements BadgeImageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BadgeImageResponse.BadgeImageUrlDTO> findBadgeImagesByNickname(String nickname) {
        return jpaQueryFactory.select(Projections.fields(BadgeImageResponse.BadgeImageUrlDTO.class,
                        badgeImage.badgeImageS3SavedURL))
                .from(badgeImage)
                .where(badgeImage.member.nickname.eq(nickname))
                .orderBy(badgeImage.id.desc())
                .fetch();
    }
}
