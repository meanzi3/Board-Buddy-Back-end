package sumcoda.boardbuddy.repository.publicDistrict;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.PublicDistrict;

import java.util.Optional;

@Repository
public interface PublicDistrictRepository extends JpaRepository<PublicDistrict, Long>, PublicDistrictRepositoryCustom {
    Optional<PublicDistrict> findBySidoAndSggAndEmd(String sido, String sgg, String emd);
}