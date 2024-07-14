package sumcoda.boardbuddy.dto;

import lombok.*;

public class MemberRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RegisterDTO {

        private String username;

        private String password;

        private String nickname;

        private String email;

        private String phoneNumber;

        private String sido;

        private String dong;

        private String latitude;

        private String longitude;

        @Builder
        public RegisterDTO(String username, String password, String nickname, String email, String phoneNumber, String sido, String dong, String latitude, String longitude) {
            this.username = username;
            this.password = password;
            this.nickname = nickname;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.sido = sido;
            this.dong = dong;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
