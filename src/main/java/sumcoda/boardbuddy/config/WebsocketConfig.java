package sumcoda.boardbuddy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 메시지 브로커 설정
     *
     * @param registry 메시지 브로커 레지스트리
     **/
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
      registry.setApplicationDestinationPrefixes("/v1/ws-stomp/publication");

      registry.enableSimpleBroker("/v1/ws-stomp/reception");
    }

    /**
     * STOMP 엔드포인트 등록
     *
     * @param registry STOMP 엔드포인트 레지스트리
     **/
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
      registry.addEndpoint("/v1/ws-stomp/connection")
              .setAllowedOriginPatterns("*");
//              .withSockJS();
    }
}
