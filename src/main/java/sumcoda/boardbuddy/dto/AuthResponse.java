package sumcoda.boardbuddy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sumcoda.boardbuddy.enumerate.Role;

public class AuthResponse {

    @Getter
    @NoArgsConstructor
    public static class ProfileDTO {

        private String username;

        private String password;

        private Role role;

        @Builder
        public ProfileDTO(String username, String password, Role role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }
}
