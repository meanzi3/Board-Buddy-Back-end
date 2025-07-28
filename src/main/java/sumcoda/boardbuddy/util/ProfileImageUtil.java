package sumcoda.boardbuddy.util;


import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
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

    /**
     * MultipartFile을 바이트 배열로 변환하여 RequestBody로 변환
     *
     * @param profileImageFile 업로드된 프로필 이미지 파일
     * @return S3 업로드 등에 사용 가능한 RequestBody 객체
     * @throws IOException 파일 읽기 중 오류 발생 시
     */
    @NotNull
    public static RequestBody convertRequestBody(MultipartFile profileImageFile) throws IOException {
        return RequestBody.fromBytes(profileImageFile.getBytes());
    }

    /**
     * 주어진 파일과 버킷 정보를 바탕으로 S3에 객체를 업로드하기 위한 PutObjectRequest를 생성
     *
     * @param profileImageFile 업로드할 파일(MultipartFile)
     * @param bucketName 대상 S3 버킷 이름
     * @param s3PutRequestKey S3에 저장될 객체의 키(경로)
     * @return 구성된 PutObjectRequest 객체
     */
    public static PutObjectRequest buildPutObjectRequest(MultipartFile profileImageFile, String bucketName, String s3PutRequestKey) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3PutRequestKey)
                .contentType(profileImageFile.getContentType())   // (Optional) Content-Type 설정
                .build();
    }

    /**
     * 지정된 버킷과 객체 키를 기반으로 S3에서 객체를 삭제하기 위한 DeleteObjectRequest를 생성
     *
     * @param bucketName 삭제할 대상 S3 버킷 이름
     * @param s3DeleteRequestKey 삭제할 객체의 키(경로)
     * @return 구성된 DeleteObjectRequest 객체
     */
    public static DeleteObjectRequest buildDeleteObjectRequest(String bucketName, String s3DeleteRequestKey) {
        return DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(s3DeleteRequestKey)
                .build();
    }
}
