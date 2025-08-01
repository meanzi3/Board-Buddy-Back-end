package sumcoda.boardbuddy.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.security.interfaces.RSAPrivateKey;

import static sumcoda.boardbuddy.util.CloudFrontUtil.*;

@Getter
@Configuration
public class CloudFrontConfig {

    @Value("${spring.cloud.aws.cloud-front.domain}")
    private String domain;

    @Value("${spring.cloud.aws.cloud-front.key-pair-id}")
    private String keyPairId;

    @Value("${spring.cloud.aws.cloud-front.url-expiration-minutes}")
    private int urlExpirationMinutes;

    @Value("${spring.cloud.aws.cloud-front.secret.region}")
    private String secretRegion;

    @Value("${spring.cloud.aws.cloud-front.secret.name}")
    private String secretName;

    @Value("${spring.cloud.aws.cloud-front.secret.key-value-name}")
    private String secretKeyValueName;



    @Bean
    public CloudFrontUtilities cloudFrontUtilities() {
        return CloudFrontUtilities.create();
    }

    @Bean
    public SecretsManagerClient secretsManagerClient() {
        return SecretsManagerClient.builder()
                .region(Region.of(this.secretRegion)).build();
    }

    @Bean
    public RSAPrivateKey cloudFrontRSAPrivateKey(SecretsManagerClient secretsManagerClient) throws Exception {
        //주어진 시크릿 이름(secretName)을 기반으로 Secrets Manager에서
        // 시크릿 값을 가져오기 위한 GetSecretValueRequest 객체를 생성하여 반환
        GetSecretValueRequest getSecretValueRequest = getGetSecretValueRequest(this.secretName);

        String secretString = secretsManagerClient.getSecretValue(getSecretValueRequest).secretString();

        // Secrets Manager에서 반환된 JSON 문자열(secretString)에서
        // 지정한 키(secretKeyValueName)에 매핑된 PEM 형태의 private key 값을 추출하여 반환
        String privateKeyPem = getPrivateKeyPem(secretString, this.secretKeyValueName);

        // PEM 형식의 RSA private key 문자열(privateKeyPem)을 파싱하여 RSAPrivateKey 객체로
        // 변환하여 반환헤더/푸터를 제거하고, 모든 공백/개행을 정리한 뒤 Base64 디코딩하여 키 스펙을 생성
        return getRsaPrivateKey(privateKeyPem);
    }
}
