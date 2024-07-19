package sumcoda.boardbuddy.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SendSMSCertificationDTO {

        private String phoneNumber;

        @Builder
        public SendSMSCertificationDTO(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ValidateSMSCertificationDTO {

        private String phoneNumber;

        private String certificationNumber;

        @Builder
        public ValidateSMSCertificationDTO(String phoneNumber, String certificationNumber) {
            this.phoneNumber = phoneNumber;
            this.certificationNumber = certificationNumber;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginDTO {

        private String username;

        private String password;

        @Builder
        public LoginDTO(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ValidatePasswordDTO {

        private String password;

        @Builder
        public ValidatePasswordDTO(String password) {
            this.password = password;
        }
    }
}
