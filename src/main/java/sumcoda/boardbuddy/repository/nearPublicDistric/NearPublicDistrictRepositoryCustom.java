package sumcoda.boardbuddy.repository.nearPublicDistric;

import sumcoda.boardbuddy.dto.NearPublicDistrictResponse;

import java.util.List;

public interface NearPublicDistrictRepositoryCustom {
    List<NearPublicDistrictResponse.InfoDTO> findInfoDTOsByPublicDistrictId(Long publicDistrictId);

    List<NearPublicDistrictResponse.LocationDTO> findLocationDTOsByPublicDistrictIdAndRadius(Long publicDistrictId, Integer radius);
}
