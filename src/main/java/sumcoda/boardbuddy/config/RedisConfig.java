package sumcoda.boardbuddy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setPassword(redisPassword);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return stringRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        // RedisTemplate 객체 생성
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // redisConnectionFactory() 를 RedisTemplate 에 설정하므로 Redis 서버와 연결할 수 있음
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        // 키, 해시 키, 해시 값을 모두 문자열로 직렬화 및 역직렬하기 위해 시리얼라이저 설정
        // RedisTemplate 의 키 시리얼라이저 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // RedisTemplate 의 해시 키 시리얼라이저 설정
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // RedisTemplate 의 해시 값 시리얼라이저 설정
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
