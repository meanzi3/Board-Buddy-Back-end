package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewResponse {

    @Getter
    @NoArgsConstructor
    public static class UserDTO {

        private String profileImageS3SavedURL;

        private Integer rank;

        private String nickname;

        private boolean hasReviewed;

        @Builder
        public UserDTO(String profileImageS3SavedURL, Integer rank, String nickname, boolean hasReviewed) {
            this.profileImageS3SavedURL = profileImageS3SavedURL;
            this.rank = rank;
            this.nickname = nickname;
            this.hasReviewed = hasReviewed;
        }
    }
}
