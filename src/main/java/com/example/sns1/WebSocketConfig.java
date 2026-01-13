package com.example.sns1;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 안드로이드 앱용
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("http://localhost:8080", "http://192.168.0.27:8080");

        // 웹 브라우저용 (SockJS 사용)
        registry.addEndpoint("/ws-stomp-web") 
                .setAllowedOriginPatterns("http://localhost:8080", "http://192.168.0.27:8080")
                .withSockJS(); 
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        //registry.setApplicationDestinationPrefixes("/pub");
    }
}
