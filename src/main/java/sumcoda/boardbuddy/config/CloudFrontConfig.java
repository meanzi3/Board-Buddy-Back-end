package sumcoda.boardbuddy.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Configuration
@ConfigurationProperties(prefix = "spring.cloud.aws.cloud-front")
@Getter
public class CloudFrontConfig {

    private String domain;

    private String keyPairId;

    // PEM 문자열 직접 주입
    private String privateKeyPem;

    private int urlExpirationMinutes;

    @Bean
    public CloudFrontUtilities cloudFrontUtilities() {
        return CloudFrontUtilities.create();
    }

    @Bean
    public RSAPrivateKey cloudFrontRSAPrivateKey() throws Exception {
        // PEM 헤더/스페이스 제거 후 Base64 디코드
        String base64 = privateKeyPem
                .replace("-----BEGIN (.*)-----", "")
                .replace("-----END (.*)-----", "")
                .replaceAll("\\s+", "");

        byte[] der = Base64.getDecoder().decode(base64);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);

        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

}
