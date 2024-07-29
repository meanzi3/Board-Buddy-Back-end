package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardCafeResponse {

    @Getter
    @NoArgsConstructor
    public static class InfoDTO {

        private String addressName;

        private Integer distance;

        private Integer id;

        private String phone;

        private String placeName;

        private String placeUrl;

        private String roadAddressName;

        private Double x;

        private Double y;

        private String sido;

        private String sigu;

        private String dong;

        @Builder
        public InfoDTO(String addressName, Integer distance, Integer id, String phone, String placeName, String placeUrl, String roadAddressName, Double x, Double y, String sido, String sigu, String dong) {
            this.addressName = addressName;
            this.distance = distance;
            this.id = id;
            this.phone = phone;
            this.placeName = placeName;
            this.placeUrl = placeUrl;
            this.roadAddressName = roadAddressName;
            this.x = x;
            this.y = y;
            this.sido = sido;
            this.sigu = sigu;
            this.dong = dong;
        }
    }
}
