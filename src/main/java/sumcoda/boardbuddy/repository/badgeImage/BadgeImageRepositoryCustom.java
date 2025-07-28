package sumcoda.boardbuddy.repository.badgeImage;

import sumcoda.boardbuddy.dto.fetch.BadgeImageInfoProjection;

import java.util.List;

public interface BadgeImageRepositoryCustom {

    List<BadgeImageInfoProjection> findBadgeImagesByNickname(String nickname);
}
