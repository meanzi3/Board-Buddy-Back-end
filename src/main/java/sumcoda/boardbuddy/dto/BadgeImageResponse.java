package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BadgeImageResponse {

    @Getter
    @NoArgsConstructor
    public static class BadgeImageInfosDTO {

        private String badgeImageS3SavedURL;

        private String badgeYearMonth;

        @Builder
        public BadgeImageInfosDTO (String badgeImageS3SavedURL, String badgeYearMonth) {
            this.badgeImageS3SavedURL = badgeImageS3SavedURL;
            this.badgeYearMonth = badgeYearMonth;
        }
    }
}
