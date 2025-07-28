package sumcoda.boardbuddy.util;


import java.util.UUID;

public final class ProfileImageUtil {

    private static final String PROFILE_IMAGE_PREFIX = "prod/images/profiles/";


    private ProfileImageUtil() {
        /* 유틸 클래스라 인스턴스화 금지 */
    }

    /**
     * 주어진 프로필 이미지 이름으로 프로필 이미지 요청 키를 생성
     *
     * @param s3SavedObjectName S3에 저장된 프로필 이미지 이름
     * @return "prod/images/profile/UUID.png" 같은 완전한 파일 경로
     */
    public static String buildProfileImageS3RequestKey(String s3SavedObjectName) {

        return PROFILE_IMAGE_PREFIX + s3SavedObjectName;
    }

    // S3에 저장할 파일 이름 생성
    public static String buildS3SavedObjectName(String originalFilename) {

        String ext = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        }

        // 2. UUID + 확장자 조합
        return UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
    }



}
