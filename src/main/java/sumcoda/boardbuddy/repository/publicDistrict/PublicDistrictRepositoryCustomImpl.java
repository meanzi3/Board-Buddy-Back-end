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

    public Optional<PublicDistrictResponse.LocationDTO> findLocationDTOBySidoAndSggAndEmd(String sido, String sgg, String emd) {

        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(PublicDistrictResponse.LocationDTO.class,
                        publicDistrict.sido,
                        publicDistrict.sgg,
                        publicDistrict.emd))
                .from(publicDistrict)
                .where(publicDistrict.sido.eq(sido)
                        .and(publicDistrict.sgg.eq(sgg))
                        .and(publicDistrict.emd.eq(emd)))
                .fetchOne());
    }

    @Override
    public List<PublicDistrictResponse.InfoDTO> findAllPublicDistrictInfoDTOs() {

        return jpaQueryFactory
                .select(Projections.fields(PublicDistrictResponse.InfoDTO.class,
                        publicDistrict.sido,
                        publicDistrict.sgg,
                        publicDistrict.emd,
                        publicDistrict.latitude,
                        publicDistrict.longitude))
                .from(publicDistrict)
                .fetch();
    }

    public Optional<PublicDistrictResponse.LocationWithIdDTO> findLocationWithIdDTOBySidoAndSggAndEmd(String sido, String sgg, String emd) {

        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(PublicDistrictResponse.LocationWithIdDTO.class,
                        publicDistrict.sido,
                        publicDistrict.sgg,
                        publicDistrict.emd,
                        publicDistrict.id))
                .from(publicDistrict)
                .where(publicDistrict.sido.eq(sido)
                        .and(publicDistrict.sgg.eq(sgg))
                        .and(publicDistrict.emd.eq(emd)))
                .fetchOne());
    }
}
