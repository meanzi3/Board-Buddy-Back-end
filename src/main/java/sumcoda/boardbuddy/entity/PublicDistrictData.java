package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PublicDistrictData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 시, 도
    @Column(nullable = false)
    private String sido;

    // 시, 구
    @Column(nullable = false)
    private String sigu;

    // 동
    @Column(nullable = false)
    private String dong;

    // 위도
    @Column(nullable = false)
    private Double latitude;

    // 경도
    @Column(nullable = false)
    private Double longitude;

    @Builder
    public PublicDistrictData(String sido, String sigu, String dong, Double latitude, Double longitude) {
        this.sido = sido;
        this.sigu = sigu;
        this.dong = dong;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성
    public static PublicDistrictData createPublicDistrictData(String sido, String sigu, String dong, Double latitude, Double longitude) {
        return PublicDistrictData.builder()
                .sido(sido)
                .sigu(sigu)
                .dong(dong)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
