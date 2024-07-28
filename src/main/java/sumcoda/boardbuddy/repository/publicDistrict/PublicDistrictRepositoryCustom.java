package sumcoda.boardbuddy.repository.publicDistrict;

import sumcoda.boardbuddy.dto.PublicDistrictResponse;

import java.util.List;
import java.util.Optional;

public interface PublicDistrictRepositoryCustom {

    Optional<PublicDistrictResponse.LocationDTO> findLocationDTOBySidoAndSiguAndDong(String sido, String sigu, String dong);

    List<PublicDistrictResponse.InfoDTO> findAllPublicDistrictInfoDTOs();

    Optional<PublicDistrictResponse.LocationWithIdDTO> findLocationWithIdDTOBySidoAndSiguAndDong(String sido, String sigu, String dong);
}
