package sumcoda.boardbuddy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sumcoda.boardbuddy.interceptor.AuthenticationInterceptor;
import sumcoda.boardbuddy.interceptor.HttpMethodFilteringInterceptor;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;

    // 2) gather-articles 관련 엔드포인트
    private static final List<String> GATHER_ARTICLE_PATHS = List.of(
            "/api/gather-articles",
            "/api/gather-articles/*",
            "/api/gather-articles/*/comments"
    );

    // 3) 전체 /api/** 중에서 exclude할 경로들
    private static final List<String> GLOBAL_EXCLUDES =
            Stream.concat(
                    GATHER_ARTICLE_PATHS.stream(),
                    List.of( "/api/auth/register",
                                    "/api/auth/username/check",
                                    "/api/auth/nickname/check",
                                    "/api/auth/sms-certifications/send",
                                    "/api/auth/sms-certifications/verify",
                                    "/api/auth/login",
                                    "/api/oauth2/**",
                                    "/api/login/oauth2/code/**",
                                    "/api/board-cafes",
                                    "/api/regions/**",
                                    "/api/rankings")
                            .stream()
            ).toList();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1) gather-articles 전용: POST/PUT/DELETE만 인터셉트
        registry.addInterceptor(
                        new HttpMethodFilteringInterceptor(
                                authenticationInterceptor,
                                HttpMethod.POST.name(),
                                HttpMethod.PUT.name(),
                                HttpMethod.DELETE.name()
                        )
                )
                .addPathPatterns(GATHER_ARTICLE_PATHS.toArray(String[]::new));

        // 2) 공통 API: /api/** 전체 인터셉트
        //    -> gather-articles 관련과 regions, rankings... 는 제외
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(GLOBAL_EXCLUDES.toArray(String[]::new));


    }

    // 로컬 테스트용 저장공간 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/uploadFiles/badgeImages/**")
                .addResourceLocations("file:src/main/resources/static/uploadFiles/badgeImages/")
                .setCachePeriod(60 * 60 * 24 * 365);
    }
}
