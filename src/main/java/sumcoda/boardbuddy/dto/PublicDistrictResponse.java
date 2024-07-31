package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PublicDistrictResponse {

    @Getter
    @NoArgsConstructor
    public static class InfoDTO {

        private String sido;
        private String sgg;
        private String emd;
        private Double longitude;
        private Double latitude;

        @Builder
        public InfoDTO(String sido, String sgg, String emd, Double longitude, Double latitude) {
            this.sido = sido;
            this.sgg = sgg;
            this.emd = emd;
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class LocationDTO {

        private String sido;
        private String sgg;
        private String emd;

        @Builder
        public LocationDTO(String sido, String sgg, String emd) {
            this.sido = sido;
            this.sgg = sgg;
            this.emd = emd;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class IdDTO {

        private Long id;

        @Builder
        public IdDTO(Long id) {
            this.id = id;
        }
    }
}
