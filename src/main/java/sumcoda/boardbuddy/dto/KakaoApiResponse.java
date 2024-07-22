package sumcoda.boardbuddy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class KakaoApiResponse {

    @Getter
    @NoArgsConstructor
    public static class KeywordSearchDTO {

        @JsonProperty("documents") // json array mapping
        private List<DocumentDTO> documentDTOList;

        @JsonProperty("meta") // json object mapping
        private MetaDTO metaDTO;

        @Builder
        public KeywordSearchDTO(List<DocumentDTO> documentDTOList, MetaDTO metaDTO) {
            this.documentDTOList = documentDTOList;
            this.metaDTO = metaDTO;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DocumentDTO {

        @JsonProperty("address_name") // 스테이크를 카멜로 매핑
        private String addressName; // 전체 지번 주소

        @JsonProperty("category_name")
        private String categoryName; // 카테고리 이름

        @JsonProperty("distance")
        private Integer distance; // 중심좌표까지의 거리

        @JsonProperty("id")
        private Integer id; // 장소 ID

        @JsonProperty("phone")
        private String phone; // 전화번호

        @JsonProperty("place_name")
        private String placeName; // 장소명

        @JsonProperty("place_url")
        private String placeUrl; // 장소 상세페이지 URL

        @JsonProperty("road_address_name")
        private String roadAddressName; // 전체 도로명 주소

        @JsonProperty("x")
        private Double x; // 경도

        @JsonProperty("y")
        private Double y; // 위도

        @Builder
        public DocumentDTO(String addressName, String categoryName, Integer distance, Integer id, String phone, String placeName, String placeUrl, String roadAddressName, Double x, Double y) {
            this.addressName = addressName;
            this.categoryName = categoryName;
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

    @Getter
    @NoArgsConstructor
    public static class MetaDTO {

        @JsonProperty("is_end") // 스테이크를 카멜로 매핑
        private Boolean isEnd; // 페이지 끝 여부

        @Builder
        public MetaDTO(Boolean isEnd) {
            this.isEnd = isEnd;
        }
    }
}
