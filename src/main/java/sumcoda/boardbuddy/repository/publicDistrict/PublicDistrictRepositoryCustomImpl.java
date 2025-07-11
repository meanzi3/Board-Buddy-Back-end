//package sumcoda.boardbuddy.repository.publicDistrict;
//
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import sumcoda.boardbuddy.dto.PublicDistrictResponse;
//
//import java.util.List;
//import java.util.Optional;
//
//import static sumcoda.boardbuddy.entity.QPublicDistrict.publicDistrict;

/**
 * @apiNote 현재는 사용률 저조로 기능이 비활성화된 상태
 *          추후 사용자 요청 또는 트래픽 증가 시 다시 활성화될 수 있음
 */
//@RequiredArgsConstructor
//public class PublicDistrictRepositoryCustomImpl implements PublicDistrictRepositoryCustom {
//
//    private final JPAQueryFactory jpaQueryFactory;
//
//    @Override
//    public List<PublicDistrictResponse.InfoWithIdDTO> findAllInfoWithIdDTOs() {
//
//        return jpaQueryFactory
//                .select(Projections.fields(PublicDistrictResponse.InfoWithIdDTO.class,
//                        publicDistrict.id,
//                        publicDistrict.sido,
//                        publicDistrict.sgg,
//                        publicDistrict.emd,
//                        publicDistrict.longitude,
//                        publicDistrict.latitude))
//                .from(publicDistrict)
//                .fetch();
//    }
//
//    @Override
//    public List<PublicDistrictResponse.InfoDTO> findAllInfoDTOs() {
//
//        return jpaQueryFactory
//                .select(Projections.fields(PublicDistrictResponse.InfoDTO.class,
//                        publicDistrict.sido,
//                        publicDistrict.sgg,
//                        publicDistrict.emd,
//                        publicDistrict.longitude,
//                        publicDistrict.latitude))
//                .from(publicDistrict)
//                .fetch();
//    }
//
//    @Override
//    public List<PublicDistrictResponse.InfoDTO> findInfoDTOsByEmd(String emd) {
//
//        return jpaQueryFactory
//                .select(Projections.fields(PublicDistrictResponse.InfoDTO.class,
//                        publicDistrict.sido,
//                        publicDistrict.sgg,
//                        publicDistrict.emd,
//                        publicDistrict.latitude,
//                        publicDistrict.longitude))
//                .from(publicDistrict)
//                .where(publicDistrict.emd.contains(emd))
//                .orderBy(publicDistrict.sido.asc())
//                .fetch();
//    }
//
//    public Optional<PublicDistrictResponse.IdDTO> findIdDTOBySidoAndSggAndEmd(String sido, String sgg, String emd) {
//
//        return Optional.ofNullable(jpaQueryFactory
//                .select(Projections.fields(PublicDistrictResponse.IdDTO.class, publicDistrict.id))
//                .from(publicDistrict)
//                .where(publicDistrict.sido.eq(sido)
//                        .and(publicDistrict.sgg.eq(sgg))
//                        .and(publicDistrict.emd.eq(emd)))
//                .fetchOne());
//    }
//
//    @Override
//    public Optional<PublicDistrictResponse.CoordinateDTO> findCoordinateDTOBySidoAndSggAndEmd(String sido, String sgg, String emd) {
//
//        return Optional.ofNullable(jpaQueryFactory
//                .select(Projections.fields(PublicDistrictResponse.CoordinateDTO.class,
//                        publicDistrict.longitude,
//                        publicDistrict.latitude))
//                .from(publicDistrict)
//                .where(publicDistrict.sido.eq(sido)
//                        .and(publicDistrict.sgg.eq(sgg))
//                        .and(publicDistrict.emd.eq(emd)))
//                .fetchOne());
//    }
//}
