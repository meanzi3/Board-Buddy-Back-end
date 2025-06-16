package sumcoda.boardbuddy.dto;

import lombok.*;

public class MemberRequest {

    /**
     * @apiNote 임시로 활성화된 DTO 클래스
     *          앱 사용률 증가시 비활성화후에 기존 DTO 클래스 활성화 예정
     **/
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RegisterDTO {

        private String username;

        private String password;

        private String nickname;

        private String email;

        private String phoneNumber;

        @Builder
        public RegisterDTO(String username, String password, String nickname, String email, String phoneNumber) {
            this.username = username;
            this.password = password;
            this.nickname = nickname;
            this.email = email;
            this.phoneNumber = phoneNumber;
        }
    }

    /**
     * @apiNote 임시로 활성화된 DTO 클래스
     *          앱 사용률 증가시 비활성화후에 기존 DTO 클래스 활성화 예정
     **/
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OAuth2RegisterDTO {

        private String phoneNumber;

        @Builder
        public OAuth2RegisterDTO(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    /**
     * @apiNote 현재는 사용률 저조로 해당 DTO 클래스가 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가시 다시 활성화될 수 있음
     **/
//    @Getter
//    @NoArgsConstructor(access = AccessLevel.PROTECTED)
//    public static class RegisterDTO {
//
//        private String username;
//
//        private String password;
//
//        private String nickname;
//
//        private String email;
//
//        private String phoneNumber;
//
//        private String sido;
//
//        private String sgg;
//
//        private String emd;
//
//        @Builder
//        public RegisterDTO(String username, String password, String nickname, String email, String phoneNumber, String sido, String sgg, String emd) {
//            this.username = username;
//            this.password = password;
//            this.nickname = nickname;
//            this.email = email;
//            this.phoneNumber = phoneNumber;
//            this.sido = sido;
//            this.sgg = sgg;
//            this.emd = emd;
//        }
//    }

    /**
     * @apiNote 현재는 사용률 저조로 해당 DTO 클래스가 비활성화된 상태
     *          추후 사용자 요청 또는 트래픽 증가시 다시 활성화될 수 있음
     **/
//    @Getter
//    @NoArgsConstructor(access = AccessLevel.PROTECTED)
//    public static class OAuth2RegisterDTO {
//
//        private String phoneNumber;
//
//        private String sido;
//
//        private String sgg;
//
//        private String emd;
//
//        @Builder
//        public OAuth2RegisterDTO(String phoneNumber, String sido, String sgg, String emd) {
//            this.phoneNumber = phoneNumber;
//            this.sido = sido;
//            this.sgg = sgg;
//            this.emd = emd;
//        }
//    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class VerifyUsernameDuplicationDTO {

        private String username;

        @Builder
        public VerifyUsernameDuplicationDTO(String username) {
            this.username = username;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class VerifyNicknameDuplicationDTO {

        private String nickname;

        @Builder
        public VerifyNicknameDuplicationDTO(String nickname) {
            this.nickname = nickname;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LocationDTO {

        private String sido;
        private String sgg;
        private String emd;

        @Builder
        public LocationDTO(String sido, String sgg, String emd) {
            this.sido = sido;
            this.sgg = sgg;
            this.emd = emd;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RadiusDTO {

        private Integer radius;

        @Builder
        public RadiusDTO(Integer radius) {
            this.radius = radius;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateProfileDTO {

        private String nickname;

        private String password;

        private String phoneNumber;

        private String description;

        @Builder
        public UpdateProfileDTO(String nickname, String password, String phoneNumber, String description) {
            this.nickname = nickname;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.description = description;
        }
    }
}

