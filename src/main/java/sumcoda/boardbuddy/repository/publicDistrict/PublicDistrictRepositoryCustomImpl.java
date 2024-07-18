package sumcoda.boardbuddy.repository.publicDistrict;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sumcoda.boardbuddy.dto.PublicDistrictResponse;

import java.util.List;
import java.util.Optional;

import static sumcoda.boardbuddy.entity.QPublicDistrict.publicDistrict;

@RequiredArgsConstructor
public class PublicDistrictRepositoryCustomImpl implements PublicDistrictRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<PublicDistrictResponse.LocationDTO> findOneBySidoAndSiguAndDong(String sido, String sigu, String dong) {

        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(PublicDistrictResponse.LocationDTO.class,
                        publicDistrict.sido,
                        publicDistrict.sigu,
                        publicDistrict.dong))
                .from(publicDistrict)
                .where(publicDistrict.sido.eq(sido)
                        .and(publicDistrict.sigu.eq(sigu))
                        .and(publicDistrict.dong.eq(dong)))
                .fetchOne());
    }

    @Override
    public List<PublicDistrictResponse.PublicDistrictDTO> findAllDistricts() {

        return jpaQueryFactory
                .select(Projections.fields(PublicDistrictResponse.PublicDistrictDTO.class,
                        publicDistrict.sido,
                        publicDistrict.sigu,
                        publicDistrict.dong,
                        publicDistrict.latitude,
                        publicDistrict.longitude))
                .from(publicDistrict)
                .fetch();
    }
}
