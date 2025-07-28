package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

public class MemberResponse {

    @Getter
    @NoArgsConstructor
    public static class LocationWithRadiusDTO {

        private String sido;
        private String sgg;
        private String emd;
        private Integer radius;

        @Builder
        public LocationWithRadiusDTO(String sido, String sgg, String emd, Integer radius) {
            this.sido = sido;
            this.sgg = sgg;
            this.emd = emd;
            this.radius = radius;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UsernameDTO {
        private String username;

        @Builder
        public UsernameDTO(String username) {
            this.username = username;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class IdDTO {
        private Long id;

        @Builder
        public IdDTO(Long id) {
            this.id = id;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class NicknameDTO {
        private String nickname;

        @Builder
        public NicknameDTO(String nickname) {
            this.nickname = nickname;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MyLocationsDTO {
        private Map<Integer, List<LocationDTO>> locations;
        private Double longitude;
        private Double latitude;
        private Integer radius;

        @Builder
        public MyLocationsDTO(Map<Integer, List<LocationDTO>> locations, Double longitude, Double latitude, Integer radius) {
            this.locations = locations;
            this.longitude = longitude;
            this.latitude = latitude;
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
