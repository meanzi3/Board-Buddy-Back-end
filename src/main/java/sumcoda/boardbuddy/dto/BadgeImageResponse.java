package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BadgeImageResponse {

    @Getter
    @NoArgsConstructor
    public static class BadgeImageUrlDTO {

        private String badgeImageS3SavedURL;


        @Builder
        public BadgeImageUrlDTO (String badgeImageS3SavedURL) {
            this.badgeImageS3SavedURL = badgeImageS3SavedURL;
        }
    }
}
