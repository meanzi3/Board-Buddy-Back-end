package sumcoda.boardbuddy.repository.badgeImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.BadgeImage;

@Repository
public interface BadgeImageRepository extends JpaRepository<BadgeImage, Long>, BadgeImageRepositoryCustom {
}
