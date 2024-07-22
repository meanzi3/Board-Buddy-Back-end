package sumcoda.boardbuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // Kakao API 를 호출하기 위한 HTTP 클라이언트 모듈
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
