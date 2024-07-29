package sumcoda.boardbuddy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MemberResponse {

    @Getter
    @NoArgsConstructor
    public static class ProfileDTO {

        private String nickname;

        private String sido;

        private String sigu;

        private String dong;

        // phoneNumber 필드가 null 일 때 JSON 반환하지 않도록하는 어노테이션
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String phoneNumber;

        private Boolean isPhoneNumberVerified;

        private String profileImageS3SavedURL;

        @Builder
        public ProfileDTO(String nickname, String sido, String sigu, String dong, String phoneNumber, Boolean isPhoneNumberVerified, String profileImageS3SavedURL) {
            this.nickname = nickname;
            this.sido = sido;
            this.sigu = sigu;
            this.dong = dong;
            this.phoneNumber = phoneNumber;
            this.isPhoneNumberVerified = isPhoneNumberVerified;
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
        private String description;

        private Integer rank;

        private Integer buddyScore;

        private List<String> badges;

        private Integer joinCount;

        private Integer totalExcellentCount;

        private Integer totalGoodCount;

        private Integer totalBadCount;

        @Builder(toBuilder = true)
        public ProfileInfosDTO(String description, Integer rank, Integer buddyScore, List<String> badges, Integer joinCount, Integer totalExcellentCount, Integer totalGoodCount, Integer totalBadCount) {
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
        private String sigu;
        private String dong;
        private Integer radius;

        @Builder
        public LocationWithRadiusDTO(String sido, String sigu, String dong, Integer radius) {
            this.sido = sido;
            this.sigu = sigu;
            this.dong = dong;
            this.radius = radius;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UserNameDTO {
        private String username;

        @Builder
        public UserNameDTO(String username) {
            this.username = username;
        }
    }
}
