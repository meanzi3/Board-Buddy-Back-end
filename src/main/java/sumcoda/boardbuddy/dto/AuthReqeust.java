package sumcoda.boardbuddy.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthReqeust {

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
}
