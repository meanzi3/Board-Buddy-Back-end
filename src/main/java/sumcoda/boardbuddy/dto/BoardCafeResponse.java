package sumcoda.boardbuddy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardCafeResponse {

    @Getter
    @NoArgsConstructor
    public static class InfoDTO {

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("distance")
        private Integer distance;

        @JsonProperty("id")
        private Integer id;

        @JsonProperty("phone")
        private String phone;

        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("place_url")
        private String placeUrl;

        @JsonProperty("road_address_name")
        private String roadAddressName;

        @JsonProperty("x")
        private Double x;

        @JsonProperty("y")
        private Double y;

        @Builder
        public InfoDTO(String addressName, Integer distance, Integer id, String phone, String placeName, String placeUrl, String roadAddressName, Double x, Double y) {
            this.addressName = addressName;
            this.distance = distance;
            this.id = id;
            this.phone = phone;
            this.placeName = placeName;
            this.placeUrl = placeUrl;
            this.roadAddressName = roadAddressName;
            this.x = x;
            this.y = y;
        }
    }
}
