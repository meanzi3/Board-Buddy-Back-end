package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NearPublicDistrictResponse {

    @Getter
    @NoArgsConstructor
    public static class NearPublicDistrictInfoDTO {

        private String sido;
        private String sigu;
        private String dong;
        private Integer radius;

        @Builder
        public NearPublicDistrictInfoDTO(String sido, String sigu, String dong, Integer radius) {
            this.sido = sido;
            this.sigu = sigu;
            this.dong = dong;
            this.radius = radius;
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
}
