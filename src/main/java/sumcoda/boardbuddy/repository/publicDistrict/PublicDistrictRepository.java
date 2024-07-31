package sumcoda.boardbuddy.repository.publicDistrict;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumcoda.boardbuddy.entity.PublicDistrict;

import java.util.Optional;

@Repository
public interface PublicDistrictRepository extends JpaRepository<PublicDistrict, Long>, PublicDistrictRepositoryCustom {

    // 레디스 장애 발생 시 데이터베이스에서 sido, sgg, emd 로 행정구역을 조회하기 위한 쿼리
    Optional<PublicDistrict> findBySidoAndSggAndEmd(String sido, String sgg, String emd);
}