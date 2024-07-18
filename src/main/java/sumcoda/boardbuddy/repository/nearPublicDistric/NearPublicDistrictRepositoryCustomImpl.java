package sumcoda.boardbuddy.repository.nearPublicDistric;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.NearPublicDistrictResponse;

import java.util.List;

import static sumcoda.boardbuddy.entity.QNearPublicDistrict.nearPublicDistrict;

@RequiredArgsConstructor
public class NearPublicDistrictRepositoryCustomImpl implements NearPublicDistrictRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<NearPublicDistrictResponse.NearPublicDistrictDTO> findByPublicDistrictId(Long publicDistrictId) {
        return jpaQueryFactory
                .select(Projections.fields(NearPublicDistrictResponse.NearPublicDistrictDTO.class,
                        nearPublicDistrict.sido,
                        nearPublicDistrict.sigu,
                        nearPublicDistrict.dong,
                        nearPublicDistrict.radius))
                .from(nearPublicDistrict)
                .where(nearPublicDistrict.publicDistrict.id.eq(publicDistrictId))
                .fetch();
    }
}
