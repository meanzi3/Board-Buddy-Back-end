package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NearPublicDistrictResponse {

    @Getter
    @NoArgsConstructor
    public static class InfoDTO {

        private String sido;
        private String sgg;
        private String emd;
        private Integer radius;

        @Builder
        public InfoDTO(String sido, String sgg, String emd, Integer radius) {
            this.sido = sido;
            this.sgg = sgg;
            this.emd = emd;
            this.radius = radius;
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
}
