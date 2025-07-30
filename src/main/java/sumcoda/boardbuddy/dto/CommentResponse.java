package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class CommentResponse {

    @Getter
    @NoArgsConstructor
    public static class AuthorUsernameDTO {

        private String username;

        @Builder
        public AuthorUsernameDTO(String username) {
            this.username = username;
        }
    }
}