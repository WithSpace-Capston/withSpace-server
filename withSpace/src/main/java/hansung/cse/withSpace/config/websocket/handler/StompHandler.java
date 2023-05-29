package hansung.cse.withSpace.config.websocket.handler;

import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
import hansung.cse.withSpace.config.jwt.JwtTokenUtil;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.exception.jwt.TokenInvalidateException;
import hansung.cse.withSpace.exception.jwt.TokenNotFoundException;
import hansung.cse.withSpace.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    //private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RedisTemplate<String, Object> redisTemplate;


//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//
//        log.info("Stomp before handshake ");
//
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        log.info("message:" + message);
//        log.info("헤더 : " + message.getHeaders());
//        log.info("토큰 = " + accessor.getFirstNativeHeader("Authorization"));
//
//        if (StompCommand.CONNECT.equals(accessor.getCommand()) &&
//                !jwtTokenUtil.validateToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization"))
//                        .substring(7))) {
//            throw new TokenInvalidateException("웹소켓 before handshake - 토큰이 유효하지 않습니다.");
//        }
//        else{
//
//
//            log.info("성공1");
//
//            String token = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization"))
//                    .substring(7);
//
//            log.info("성공2");
//
//            String sessionId = accessor.getSessionId();
//            UUID uuid = jwtAuthenticationFilter.getUUIDFromToken(token);
//
//            log.info("성공3");
//
//
//            // 세션 정보 Redis에 저장
//            String sessionKey = "session:" + sessionId;
//            redisTemplate.opsForHash().put(sessionKey, "memberUUID", uuid);
//            log.info("Session stored in Redis - sessionId: {}, userId: {}", sessionId, uuid);
//
//        }
//        return message;
//    }
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        log.info("Stomp before handshake ");

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        log.info("message:" + message);
        log.info("헤더 : " + message.getHeaders());
        log.info("토큰 = " + accessor.getFirstNativeHeader("Authorization"));

        String token = (Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization"))
                .substring(7));

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            //jwtAuthenticationFilter.getUUIDFromToken(token);

        }

        return message;

    }
}