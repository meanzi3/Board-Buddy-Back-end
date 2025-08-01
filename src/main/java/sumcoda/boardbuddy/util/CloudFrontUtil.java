package sumcoda.boardbuddy.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public final class CloudFrontUtil {

    private CloudFrontUtil() {
        /* 유틸 클래스라 인스턴스화 금지 */
    }

    /**
     * 주어진 도메인과 객체 키(requestKey)를 조합하여 CloudFront 리소스에 접근하기 위한 전체 URL을 생성
     *
     * @param domain     CloudFront 배포 도메인
     * @param requestKey 접근할 객체의 경로 또는 키 (예: "prod/images/abc.jpg")
     * @return "https://{domain}/{requestKey}" 형식의 완전한 리소스 URL
     */
    public static String buildResourceUrl(String domain, String requestKey) {
        if (domain.endsWith("/")) {
            domain = domain.substring(0, domain.length() - 1);
        }
        return String.format("https://%s/%s", domain, requestKey);
    }

    /**
     * 현재 시점에서 지정된 분(minutes)만큼 더한 만료 시간을 계산하여 반환
     *
     * @param minutes URL 유효 기간(분 단위)
     * @return 만료 시각을 나타내는 Instant 객체
     */
    public static Instant calculateExpiration(int minutes) {
        return Instant.now().plus(minutes, ChronoUnit.MINUTES);
    }


    /**
     * 주어진 시크릿 이름(secretName)을 기반으로 Secrets Manager에서 시크릿 값을 가져오기 위한
     * GetSecretValueRequest 객체를 생성하여 반환
     *
     * @param secretName 조회할 시크릿의 아이디(Name)
     * @return 해당 시크릿을 조회할 수 있는 GetSecretValueRequest 인스턴스
     */
    public static GetSecretValueRequest getGetSecretValueRequest(String secretName) {
        return GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
    }

    /**
     * Secrets Manager에서 반환된 JSON 문자열(secretString)에서 지정한 키(secretKeyValueName)에
     * 매핑된 PEM 형태의 private key 값을 추출하여 반환
     * JSON 파싱이 실패하거나 지정된 키가 없으면 예외를 던짐
     *
     * @param secretString Secrets Manager가 반환한 시크릿의 raw JSON 문자열 (예: {"키": "값"} 형식)
     * @param secretKeyValueName JSON 내부에서 실제 private key가 들어있는 필드 이름
     * @return PEM 포맷의 private key 문자열
     * @throws IllegalStateException JSON이 유효하지 않거나 기대한 키가 없을 경우 발생
     */
    public static String getPrivateKeyPem(String secretString, String secretKeyValueName) {
        ObjectMapper om = new ObjectMapper();

        String privateKeyPem;

        JsonNode root;
        try {
            root = om.readTree(secretString);
        } catch (Exception e) {
            throw new IllegalStateException("Secrets Manager에서 받은 값이 유효한 JSON이 아닙니다.", e);
        }

        if (!root.has(secretKeyValueName)) {
            throw new IllegalStateException("Secrets Manager 시크릿에 '" + secretKeyValueName + "' 키가 없습니다. key/value 형식으로 저장해야 합니다.");
        }
        privateKeyPem = root.get(secretKeyValueName).asText();
        return privateKeyPem;
    }


    /**
     * PEM 형식의 RSA private key 문자열(privateKeyPem)을 파싱하여 RSAPrivateKey 객체로 변환하여 반환
     * 헤더/푸터를 제거하고, 모든 공백/개행을 정리한 뒤 Base64 디코딩하여 키 스펙을 생성
     *
     * @param privateKeyPem PEM 포맷의 RSA private key 문자열 (예: "-----BEGIN RSA PRIVATE KEY-----...-----END RSA PRIVATE KEY-----")
     * @return Java에서 사용 가능한 RSAPrivateKey 인스턴스
     * @throws InvalidKeySpecException 키 스펙이 유효하지 않을 경우
     * @throws NoSuchAlgorithmException RSA 알고리즘이 존재하지 않을 경우
     */
    public static RSAPrivateKey getRsaPrivateKey(String privateKeyPem) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String base64 = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decode = Base64.getDecoder().decode(base64);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);

        return (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(keySpec);
    }
}
