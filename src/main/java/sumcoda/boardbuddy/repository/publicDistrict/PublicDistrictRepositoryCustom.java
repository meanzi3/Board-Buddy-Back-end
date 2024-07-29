package sumcoda.boardbuddy.repository.publicDistrict;

import sumcoda.boardbuddy.dto.PublicDistrictResponse;

import java.util.List;
import java.util.Optional;

public interface PublicDistrictRepositoryCustom {

    Optional<PublicDistrictResponse.LocationDTO> findLocationDTOBySidoAndSggAndEmd(String sido, String sgg, String emd);

    List<PublicDistrictResponse.InfoDTO> findAllPublicDistrictInfoDTOs();

    Optional<PublicDistrictResponse.LocationWithIdDTO> findLocationWithIdDTOBySidoAndSggAndEmd(String sido, String sgg, String emd);
}
