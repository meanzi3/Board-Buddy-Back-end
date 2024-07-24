package sumcoda.boardbuddy.repository.nearPublicDistric;

import sumcoda.boardbuddy.dto.NearPublicDistrictResponse;

import java.util.List;

public interface NearPublicDistrictRepositoryCustom {
    List<NearPublicDistrictResponse.InfoDTO> findByPublicDistrictId(Long publicDistrictId);

    List<NearPublicDistrictResponse.LocationDTO> findByPublicDistrictIdAndRadius(Long publicDistrictId, Integer radius);
}
