package sumcoda.boardbuddy.repository.publicDistrict;

import sumcoda.boardbuddy.dto.PublicDistrictResponse;

import java.util.List;
import java.util.Optional;

public interface PublicDistrictRepositoryCustom {

    List<PublicDistrictResponse.InfoDTO> findAllInfoDTOs();

    List<PublicDistrictResponse.InfoDTO> findInfoDTOsByEmd(String emd);

    Optional<PublicDistrictResponse.IdDTO> findIdDTOBySidoAndSggAndEmd(String sido, String sgg, String emd);
}
