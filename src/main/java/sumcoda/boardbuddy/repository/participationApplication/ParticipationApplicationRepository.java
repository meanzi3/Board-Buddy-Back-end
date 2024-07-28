package sumcoda.boardbuddy.repository.participationApplication;

import org.springframework.data.jpa.repository.JpaRepository;
import sumcoda.boardbuddy.entity.ParticipationApplication;

import java.util.Optional;

public interface ParticipationApplicationRepository extends JpaRepository<ParticipationApplication, Long>, ParticipationApplicationRepositoryCustom {

    Optional<ParticipationApplication> findById(Long participationApplicationId);
}
