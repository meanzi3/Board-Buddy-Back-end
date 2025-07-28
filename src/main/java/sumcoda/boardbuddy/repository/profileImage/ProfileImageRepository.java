package sumcoda.boardbuddy.repository.profileImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.ProfileImage;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long>, ProfileImageRepositoryCustom {

    void deleteByS3SavedObjectName(String s3SavedObjectName);
}
