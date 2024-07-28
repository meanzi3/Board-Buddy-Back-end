package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ParticipationApplicationResponse {

    @Getter
    @NoArgsConstructor
    public static class InfoDTO {

        private Long id;

        private String nickname;

        private String profileImageS3SavedURL;

        @Builder
        public InfoDTO(Long id, String nickname, String profileImageS3SavedURL) {
            this.id = id;
            this.nickname = nickname;
            this.profileImageS3SavedURL = profileImageS3SavedURL;
        }
    }
}
