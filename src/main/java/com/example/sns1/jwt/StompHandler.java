package com.example.sns1.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            // 웹(세션) 사용자 확인
            if (accessor.getUser() != null) {
                log.info("WebSocket: 웹 세션 사용자 접속 허용");
                return message;
            }

            // 앱(JWT) 사용자 확인
            String jwtToken = accessor.getFirstNativeHeader("Authorization");
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                String token = jwtToken.substring(7);
                if (jwtTokenProvider.validateToken(token)) {
                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    accessor.setUser(auth);
                    log.info("WebSocket: 앱 JWT 사용자 접속 허용 ({})", auth.getName());
                } else {
                    throw new AccessDeniedException("유효하지 않은 JWT 토큰");
                }
            } else {
                throw new AccessDeniedException("인증 정보가 없습니다.");
            }
        }
        return message;
    }
}