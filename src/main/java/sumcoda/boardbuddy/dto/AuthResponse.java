package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.MemberRole;

public class AuthResponse {

    @Getter
    @NoArgsConstructor
    public static class ProfileDTO {

        private String username;

        private String password;

        private MemberRole memberRole;

        @Builder
        public ProfileDTO(String username, String password, MemberRole memberRole) {
            this.username = username;
            this.password = password;
            this.memberRole = memberRole;
        }
    }
}
