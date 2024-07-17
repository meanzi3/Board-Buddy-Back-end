package sumcoda.boardbuddy.repository.nearPublicDistric;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sumcoda.boardbuddy.entity.NearPublicDistrict;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NearPublicDistrictJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<NearPublicDistrict> nearPublicDistricts) {
        String sql = "INSERT INTO near_public_district (sido, sigu, dong, radius, public_district_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql,
                nearPublicDistricts,
                nearPublicDistricts.size(),
                (PreparedStatement ps, NearPublicDistrict nearPublicDistrict) -> {
                    ps.setString(1, nearPublicDistrict.getSido());
                    ps.setString(2, nearPublicDistrict.getSigu());
                    ps.setString(3, nearPublicDistrict.getDong());
                    ps.setInt(4, nearPublicDistrict.getRadius());
                    ps.setLong(5, nearPublicDistrict.getPublicDistrict().getId());
                });
    }
}
