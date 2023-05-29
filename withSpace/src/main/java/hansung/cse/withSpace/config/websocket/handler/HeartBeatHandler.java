//package hansung.cse.withSpace.config.websocket.handler;
//
//import hansung.cse.withSpace.config.jwt.JwtTokenUtil;
//import hansung.cse.withSpace.domain.Member;
//import hansung.cse.withSpace.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.messaging.support.MessageHeaderAccessor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//
//
//@Component
//@RequiredArgsConstructor
//public class HeartBeatHandler implements ChannelInterceptor {
//
//    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//    private final MemberService memberService;
//    private final RedisTemplate<String, Boolean> redisTemplate;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//
//            accessor.get
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            System.out.println("authentication = " + authentication);
//
//            String email = authentication.getName();
//
//            Member member = memberService.findByEmail(email);
//            UUID uuid = member.getUuid();
//
//            sessions.put(uuid.toString(), (WebSocketSession) accessor.getSessionAttributes().get("webSocketSession"));
//            memberService.setMemberActive(uuid);
//            redisTemplate.opsForValue().set(uuid.toString(), true, 10, TimeUnit.SECONDS);
//        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
//            String sessionId = accessor.getSessionId();
//
//            UUID uuidToRemove = null;
//            for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
//                if (sessionId.equals(entry.getValue().getId())) {
//                    uuidToRemove = UUID.fromString(entry.getKey());
//                    break;
//                }
//            }
//
//            if (uuidToRemove != null) {
//                sessions.remove(uuidToRemove.toString());
//                memberService.setMemberInActive(uuidToRemove);
//                redisTemplate.delete(uuidToRemove.toString());
//            }
//        }
//
//        return message;
//    }
//
//    @Scheduled(fixedRate = 10000)
//    public void sendHeartbeat() {
//        sessions.forEach((uuid, session) -> {
//            try {
//                session.sendMessage(new TextMessage("HEARTBEAT"));
//            } catch (IOException e) {
//                e.printStackTrace();
//                sessions.remove(uuid);
//                memberService.setMemberInActive(UUID.fromString(uuid));
//                redisTemplate.delete(uuid.toString());
//            }
//        });
//    }
//}