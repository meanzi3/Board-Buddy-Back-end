package sumcoda.boardbuddy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;
import sumcoda.boardbuddy.config.CloudFrontConfig;
import sumcoda.boardbuddy.util.CloudFrontSignedUrlUtil;

import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CloudFrontSignedUrlService {

    private final CloudFrontUtilities cloudFrontUtilities;

    private final RSAPrivateKey cloudFrontRSAPrivateKey;

    private final CloudFrontConfig cloudFrontConfig;


    /**
     * Canned Policy 방식으로 CloudFront Signed URL을 생성
     *
     * @param requestKey CloudFront로 접근할 리소스 객체의 키 (예: "/prod/images/abc.jpg")
     * @return 설정된 만료 시간이 적용된 CloudFront Signed URL
     */
    public String generateSignedUrl(String requestKey) {

        // 1. 서명 대상 URL (도메인 + 객체 경로)
        String resourceUrl = CloudFrontSignedUrlUtil.buildResourceUrl(cloudFrontConfig.getDomain(), requestKey);

        // 2. 만료 시간 계산
        Instant expiration = CloudFrontSignedUrlUtil.calculateExpiration(cloudFrontConfig.getUrlExpirationMinutes());

        // 3. Canned policy 방식으로 Signed URL 생성
        CannedSignerRequest signerReq = CannedSignerRequest.builder()
                .resourceUrl(resourceUrl)
                .privateKey(cloudFrontRSAPrivateKey)
                .keyPairId(cloudFrontConfig.getKeyPairId())
                .expirationDate(expiration)
                .build();

        return cloudFrontUtilities.getSignedUrlWithCannedPolicy(signerReq).url();
    }
}
