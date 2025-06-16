//package sumcoda.boardbuddy.repository.nearPublicDistric;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//import sumcoda.boardbuddy.entity.NearPublicDistrict;
//
//import java.sql.PreparedStatement;
//import java.util.List;
//
//@Repository
//@RequiredArgsConstructor
//public class NearPublicDistrictJdbcRepository {

//    private final JdbcTemplate jdbcTemplate;

    /**
     * @apiNote 현재는 사용률 저조로 기능이 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가 시 다시 활성화될 수 있음
     */
//    @Transactional
//    public void saveAll(List<NearPublicDistrict> nearPublicDistricts) {
//        String sql = "INSERT INTO near_public_district (sido, sgg, emd, radius, public_district_id) VALUES (?, ?, ?, ?, ?)";
//        jdbcTemplate.batchUpdate(sql,
//                nearPublicDistricts,
//                nearPublicDistricts.size(),
//                (PreparedStatement ps, NearPublicDistrict nearPublicDistrict) -> {
//                    ps.setString(1, nearPublicDistrict.getSido());
//                    ps.setString(2, nearPublicDistrict.getSgg());
//                    ps.setString(3, nearPublicDistrict.getEmd());
//                    ps.setInt(4, nearPublicDistrict.getRadius());
//                    ps.setLong(5, nearPublicDistrict.getPublicDistrict().getId());
//                });
//    }
//}
