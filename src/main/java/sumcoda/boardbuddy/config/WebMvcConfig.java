package sumcoda.boardbuddy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
                        "/api/auth/register",
                        "/api/auth/check-username",
                        "/api/auth/check-nickname",
                        "/api/auth/sms-certifications/send",
                        "/api/auth/sms-certifications/verify",
                        "/api/auth/login",
                        // Authentication 로직을 실행하지만 해당 핸들러에서는 필요 없다.
                        "/api/auth/status",
                        "/api/oauth2/authorization/google",
                        "/api/oauth2/authorization/kakao",
                        "/api/oauth2/authorization/naver"));
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
