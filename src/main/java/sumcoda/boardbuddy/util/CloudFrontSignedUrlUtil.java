package sumcoda.boardbuddy.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class CloudFrontSignedUrlUtil {

    private CloudFrontSignedUrlUtil() {
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
}
