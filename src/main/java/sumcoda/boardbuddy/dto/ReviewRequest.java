package sumcoda.boardbuddy.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.ReviewType;

public class ReviewRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReviewDTO {

        private String nickname;

        private ReviewType review;

        @Builder
        public ReviewDTO(String nickname, ReviewType review) {
            this.nickname = nickname;
            this.review = review;
        }
    }
}
