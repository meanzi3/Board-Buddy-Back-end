package sumcoda.boardbuddy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sumcoda.boardbuddy.service.MemberService;


@Configuration
@RequiredArgsConstructor
public class InitializerConfig {

    private final MemberService memberService;

    @Bean
    public ApplicationRunner initializer() {
        return args -> {
            memberService.createAdminAccount();
        };
    }
}
