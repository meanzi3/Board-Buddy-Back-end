package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NearPublicDistrictResponse {

    @Getter
    @NoArgsConstructor
    public static class NearPublicDistrictDTO {

        private String sido;
        private String sigu;
        private String dong;
        private int radius;

        @Builder
        public NearPublicDistrictDTO(String sido, String sigu, String dong, int radius) {
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
