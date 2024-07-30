package sumcoda.boardbuddy.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NearPublicDistrictRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
