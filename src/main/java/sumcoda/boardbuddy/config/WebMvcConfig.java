package sumcoda.boardbuddy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sumcoda.boardbuddy.interceptor.AuthenticationInterceptor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(List.of(
                        "/api/v1/auth/register",
                        "/api/v1/auth/username/check",
                        "/api/v1/auth/nickname/check",
                        "/api/v1/auth/sms-certifications/send",
                        "/api/v1/auth/sms-certifications/verify",
                        "/api/v1/auth/login",
                        "/api/v1/oauth2/**",
                        "/api/v1/login/oauth2/code/**",
                        "/api/v1/auth/locations/search",
                        "/api/v1/rankings"
                ));
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    // 로컬 테스트용 저장공간 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/uploadFiles/badgeImages/**")
                .addResourceLocations("file:src/main/resources/static/uploadFiles/badgeImages/")
                .setCachePeriod(60 * 60 * 24 * 365);
    }
}
