package sumcoda.boardbuddy.repository.badgeImage;

import sumcoda.boardbuddy.dto.BadgeImageResponse;

import java.util.List;

public interface BadgeImageRepositoryCustom {
    List<BadgeImageResponse.BadgeImageUrlDTO> findBadgeImagesByNickname(String nickname);
}
