package sumcoda.boardbuddy.repository.nearPublicDistric;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.NearPublicDistrict;

@Repository
public interface NearPublicDistrictRepository extends JpaRepository<NearPublicDistrict, Long>, NearPublicDistrictRepositoryCustom {
}