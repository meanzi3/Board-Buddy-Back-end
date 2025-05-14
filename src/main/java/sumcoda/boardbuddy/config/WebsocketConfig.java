package sumcoda.boardbuddy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 메시지 브로커 설정
     *
     * @param registry 메시지 브로커 레지스트리
     **/
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 발신 경로
        registry.setApplicationDestinationPrefixes("/api/v1/chat/publications");

        // 채팅방 구독 경로
        registry.enableSimpleBroker("/api/v1/chat/subscriptions");
    }

    /**
     * STOMP 엔드포인트 등록
     *
     * @param registry STOMP 엔드포인트 레지스트리
     **/
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 웹소켓 연결 경로
        registry.addEndpoint("/api/v1/chat/connection")
                .setAllowedOriginPatterns("*");
    }
}
