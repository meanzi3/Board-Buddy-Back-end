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
                .addPathPatterns(List.of("/api/**", "/api/gatherArticles", "/api/auth/oauth2/register"))
                .excludePathPatterns(List.of(
                        "/api/auth/register",
                        "/api/auth/check-username",
                        "/api/auth/check-nickname",
                        "/api/auth/sms-certifications/send",
                        "/api/auth/sms-certifications/verify",
                        "/api/auth/login",
                        "/api/oauth2/**",
                        "/api/login/oauth2/code/**"
                        // 태스트

                       ));
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/uploadFiles/badgeImages/**")
                .addResourceLocations("file:src/main/resources/static/uploadFiles/badgeImages/")
                .setCachePeriod(60 * 60 * 24 * 365);
    }
}
