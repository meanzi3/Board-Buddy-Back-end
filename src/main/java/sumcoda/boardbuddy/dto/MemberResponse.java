package sumcoda.boardbuddy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.MemberType;

import java.util.List;
import java.util.Map;

public class MemberResponse {

    @Getter
    @NoArgsConstructor
    public static class ProfileDTO {

        private String nickname;

        private String sido;

        private String sgg;

        private String emd;

        // phoneNumber 필드가 null 일 때 JSON 반환하지 않도록하는 어노테이션
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String phoneNumber;

        private Boolean isPhoneNumberVerified;

        private MemberType memberType;

        private String profileImageS3SavedURL;


        @Builder
        public ProfileDTO(String nickname, String sido, String sgg, String emd, String phoneNumber, Boolean isPhoneNumberVerified, MemberType memberType, String profileImageS3SavedURL) {
            this.nickname = nickname;
            this.sido = sido;
            this.sgg = sgg;
            this.emd = emd;
            this.phoneNumber = phoneNumber;
            this.isPhoneNumberVerified = isPhoneNumberVerified;
            this.memberType = memberType;
            this.profileImageS3SavedURL = profileImageS3SavedURL;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class RankingsDTO {
        private String nickname;
        private String profileImageS3SavedURL;

        @Builder
        public RankingsDTO(String nickname, String profileImageS3SavedURL) {
            this.nickname = nickname;
            this.profileImageS3SavedURL = profileImageS3SavedURL;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ProfileInfosDTO {
        private String profileImageS3SavedURL;

        private String description;

        private Integer rank;

        private Double buddyScore;

        private List<String> badges;

        private Integer joinCount;

        private Integer totalExcellentCount;

        private Integer totalGoodCount;

        private Integer totalBadCount;

        @Builder(toBuilder = true)
        public ProfileInfosDTO(String profileImageS3SavedURL, String description, Integer rank, Double buddyScore, List<String> badges, Integer joinCount, Integer totalExcellentCount, Integer totalGoodCount, Integer totalBadCount) {
            this.profileImageS3SavedURL = profileImageS3SavedURL;
            this.description = description;
            this.rank = rank;
            this.buddyScore = buddyScore;
            this.badges = badges;
            this.joinCount = joinCount;
            this.totalExcellentCount = totalExcellentCount;
            this.totalGoodCount = totalGoodCount;
            this.totalBadCount = totalBadCount;
        }
    }

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
