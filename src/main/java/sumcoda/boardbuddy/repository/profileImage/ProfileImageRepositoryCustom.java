package sumcoda.boardbuddy.repository.profileImage;

import sumcoda.boardbuddy.dto.fetch.ProfileImageObjectNameProjection;

import java.util.Optional;

public interface ProfileImageRepositoryCustom {

    boolean existsByUsername(String username);

    Optional<ProfileImageObjectNameProjection> findProfileImageObjectNameByUsername(String username);

}
