package sumcoda.boardbuddy.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sumcoda.boardbuddy.service.MemberService;

import java.util.TimeZone;


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

    @PostConstruct
    public void setTimeZone(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
