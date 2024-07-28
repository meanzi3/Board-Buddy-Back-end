package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PublicDistrictResponse {

    @Getter
    @NoArgsConstructor
    public static class InfoDTO {

        private String sido;
        private String sigu;
        private String dong;
        private Double latitude;
        private Double longitude;

        @Builder
        public InfoDTO(String sido, String sigu, String dong, Double latitude, Double longitude) {
            this.sido = sido;
            this.sigu = sigu;
            this.dong = dong;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class LocationDTO {

        private String sido;
        private String sigu;
        private String dong;

        @Builder
        public LocationDTO(String sido, String sigu, String dong) {
            this.sido = sido;
            this.sigu = sigu;
            this.dong = dong;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class LocationWithIdDTO {

        private String sido;
        private String sigu;
        private String dong;
        private Long id;

        @Builder
        public LocationWithIdDTO(String sido, String sigu, String dong, Long id) {
            this.sido = sido;
            this.sigu = sigu;
            this.dong = dong;
            this.id = id;
        }
    }
}
