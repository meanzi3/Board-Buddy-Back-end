package sumcoda.boardbuddy.repository.nearPublicDistric;

import sumcoda.boardbuddy.dto.NearPublicDistrictResponse;

import java.util.List;

public interface NearPublicDistrictRepositoryCustom {
    List<NearPublicDistrictResponse.NearPublicDistrictDTO> findByPublicDistrictId(Long publicDistrictId);
}
