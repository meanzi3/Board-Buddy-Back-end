package sumcoda.boardbuddy.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

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
        // 1) Secrets Manager에서 PEM 문자열 읽기
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(this.secretName)
                .build();

        String privateKeyPem = secretsManagerClient.getSecretValue(getSecretValueRequest).secretString();

        // 2) PEM 헤더/스페이스 제거 후 Base64 디코드
        String base64 = privateKeyPem
                .replace("-----BEGIN (.*)-----", "")
                .replace("-----END (.*)-----", "")
                .replaceAll("\\s+", "");

        byte[] decode = Base64.getDecoder().decode(base64);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);

        return (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(keySpec);
    }
}
