package sumcoda.boardbuddy.repository.publicDistrict;

import sumcoda.boardbuddy.dto.PublicDistrictResponse;

import java.util.List;
import java.util.Optional;

public interface PublicDistrictRepositoryCustom {
    Optional<PublicDistrictResponse.LocationDTO> findOneBySidoAndSiguAndDong(String sido, String sigu, String dong);
    List<PublicDistrictResponse.PublicDistrictDTO> findAllDistricts();
}
