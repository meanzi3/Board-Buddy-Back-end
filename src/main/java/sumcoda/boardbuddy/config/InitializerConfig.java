package sumcoda.boardbuddy.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sumcoda.boardbuddy.service.BadgeImageService;
import sumcoda.boardbuddy.service.MemberService;

import java.time.YearMonth;
import java.util.TimeZone;


@Configuration
@RequiredArgsConstructor
public class InitializerConfig {

    private final MemberService memberService;

    private final BadgeImageService badgeImageService;

    @Bean
    public ApplicationRunner initializer() {
        return args -> {
            memberService.createAdminAccount();
            memberService.createInitTestAccounts();
            badgeImageService.assignBadgesToInitTestMembers(YearMonth.now().minusMonths(1));
        };
    }

    @PostConstruct
    public void setTimeZone(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
