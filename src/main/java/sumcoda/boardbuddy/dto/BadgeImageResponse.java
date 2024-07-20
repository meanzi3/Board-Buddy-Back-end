package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class BadgeImageResponse {

    @Getter
    @NoArgsConstructor
    public static class BadgeImageListDTO {

        private List<String> badges;

        @Builder
        public BadgeImageListDTO (List<String> badges) {
            this.badges = badges;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BadgeImageUrlDTO {
        //로컬 경로
        private String localSavedFileURL;

        //AWS 경로
//        private String awsS3SavedFileURL;


        @Builder
        public BadgeImageUrlDTO (String localSavedFileURL) {
            this.localSavedFileURL = localSavedFileURL;
        }

        // AWS 사용 시 활용
//        @Builder
//        public BadgeImageUrlDTO (String awsS3SavedFileURL) {
//            this.awsS3SavedFileURL = awsS3SavedFileURL;
//        }
    }
}
