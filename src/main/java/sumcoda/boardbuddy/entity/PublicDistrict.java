package sumcoda.boardbuddy.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PublicDistrict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 시, 도
    @Column(nullable = false)
    private String sido;

    // 시, 군, 구
    @Column(nullable = false)
    private String sgg;

    // 읍, 면, 동
    @Column(nullable = false)
    private String emd;

    // 경도
    @Column(nullable = false)
    private Double longitude;

    // 위도
    @Column(nullable = false)
    private Double latitude;

    // 연관 관계 설정
    @OneToMany(mappedBy = "publicDistrict")
    private List<NearPublicDistrict> nearPublicDistricts = new ArrayList<>();

    @Builder
    public PublicDistrict(Long id, String sido, String sgg, String emd, Double longitude, Double latitude) {
        this.id = id;
        this.sido = sido;
        this.sgg = sgg;
        this.emd = emd;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성(id 를 제외한 생성자)
    public static PublicDistrict buildPublicDistrict(String sido, String sgg, String emd, Double longitude, Double latitude) {
        return PublicDistrict.builder()
                .sido(sido)
                .sgg(sgg)
                .emd(emd)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

    // 직접 빌더 패턴의 생성자를 활용하지 말고 해당 메서드를 활용하여 엔티티 생성(id 를 포함한 생성자)
    public static PublicDistrict buildIdWithPublicDistrict(Long id, String sido, String sgg, String emd, Double longitude, Double latitude) {
        return PublicDistrict.builder()
                .id(id)
                .sido(sido)
                .sgg(sgg)
                .emd(emd)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

    // PublicDistrict 1 <-> N NearPublicDistrict
    // 양방향 연관관계 편의 메서드
    public void addNearPublicDistrict(NearPublicDistrict nearPublicDistrict) {
        this.nearPublicDistricts.add(nearPublicDistrict);

        if (nearPublicDistrict.getPublicDistrict() != this) {
            nearPublicDistrict.assignPublicDistrict(this);
        }
    }
}
