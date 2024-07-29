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
        private Double latitude;
        private Double longitude;

        @Builder
        public InfoDTO(String sido, String sgg, String emd, Double latitude, Double longitude) {
            this.sido = sido;
            this.sgg = sgg;
            this.emd = emd;
            this.latitude = latitude;
            this.longitude = longitude;
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
    public static class LocationWithIdDTO {

        private String sido;
        private String sgg;
        private String emd;
        private Long id;

        @Builder
        public LocationWithIdDTO(String sido, String sgg, String emd, Long id) {
            this.sido = sido;
            this.sgg = sgg;
            this.emd = emd;
            this.id = id;
        }
    }
}
