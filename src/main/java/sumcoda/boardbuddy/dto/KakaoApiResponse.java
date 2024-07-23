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

        @JsonProperty("documents")
        private List<DocumentDTO> documentDTOList;

        @JsonProperty("meta")
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
        // 전체 지번 주소, 스네이크 표기법을 카멜 표기법으로 매핑
        @JsonProperty("address_name")
        private String addressName;
        // 카테고리 이름
        @JsonProperty("category_name")
        private String categoryName;
        // 중심좌표까지의 거리
        @JsonProperty("distance")
        private Integer distance;
        // 장소 ID
        @JsonProperty("id")
        private Integer id;
        // 전화번호
        @JsonProperty("phone")
        private String phone;
        // 장소명
        @JsonProperty("place_name")
        private String placeName;
        // 장소 상세페이지 URL
        @JsonProperty("place_url")
        private String placeUrl;
        // 전체 도로명 주소
        @JsonProperty("road_address_name")
        private String roadAddressName;
        // 경도
        @JsonProperty("x")
        private Double x;
        // 위도
        @JsonProperty("y")
        private Double y;

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
        // 페이지 끝 여부
        @JsonProperty("is_end")
        private Boolean isEnd;

        @Builder
        public MetaDTO(Boolean isEnd) {
            this.isEnd = isEnd;
        }
    }
}
