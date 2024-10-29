package com.example.cloneverytime.listener;

import com.example.cloneverytime.model.ChatMessage;
import com.example.cloneverytime.model.MessageType;
import com.example.cloneverytime.model.User;
import com.example.cloneverytime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;

@Component
public class WebSocketEventListener {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private SimpMessagingTemplate messagingTemplate;

   @EventListener
   public void handleWebSocketConnectListener(SessionConnectedEvent event) {
       System.out.println("새로운 WebSocket 연결");
   }

   @EventListener
   public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
       StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

       Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
       if (sessionAttributes != null) {
           String username = (String) sessionAttributes.get("username");
           if (username != null) {
               System.out.println("사용자 연결 해제: " + username);

               User user = userRepository.findByName(username);
               if (user != null) {
                   user.setOnline(false);
                   userRepository.save(user);
               }

               ChatMessage chatMessage = new ChatMessage();
               chatMessage.setType(MessageType.LEAVE);

               ChatMessage.UserInfo userInfo = new ChatMessage.UserInfo();
               userInfo.setName(username);
               chatMessage.setUser(userInfo);

               // 사용자 퇴장 메시지 전송
               messagingTemplate.convertAndSend("/topic/public", chatMessage);

               // 업데이트된 사용자 목록 전송
               List<User> onlineUsers = userRepository.findByOnline(true);
               messagingTemplate.convertAndSend("/topic/users", onlineUsers);
           }
       }
   }
}
