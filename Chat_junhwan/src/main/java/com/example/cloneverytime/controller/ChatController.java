package com.example.cloneverytime.controller;

import com.example.cloneverytime.model.ChatMessage;
import com.example.cloneverytime.model.MessageType;
import com.example.cloneverytime.model.User;
import com.example.cloneverytime.repository.ChatRepository;
import com.example.cloneverytime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private ChatRepository chatRepository;

   @Autowired
   private org.springframework.messaging.simp.SimpMessagingTemplate messagingTemplate;

   @MessageMapping("/chat.sendMessage")
   @SendTo("/topic/public")
   public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
       System.out.println("Received message: " + chatMessage);
       ChatMessage.UserInfo userInfo = chatMessage.getUser();

       if (userInfo == null || userInfo.getName() == null) {
           System.out.println("UserInfo or user name is null");
           return null;
       }

       User user = userRepository.findByName(userInfo.getName());
       if (user == null) {
           user = new User();
           user.setName(userInfo.getName());
           user.setOnline(true);
           userRepository.save(user);
       }

       userInfo.setId(user.getId());
       chatMessage.setUser(userInfo);
       chatMessage.setType(MessageType.CHAT);
       chatRepository.save(chatMessage);

       return chatMessage;
   }

   @MessageMapping("/chat.addUser")
   @SendTo("/topic/public")
   public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
       System.out.println("Received addUser message: " + chatMessage);
       ChatMessage.UserInfo userInfo = chatMessage.getUser();
       String username = userInfo.getName();

       if (username == null || username.isEmpty()) {
           System.out.println("Username is null or empty");
           return null;
       }

       User user = userRepository.findByName(username);
       if (user == null) {
           user = new User();
           user.setName(username);
       }
       user.setOnline(true);
       userRepository.save(user);

       userInfo.setId(user.getId());
       chatMessage.setUser(userInfo);

       // Null 체크 및 초기화
       Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
       if (sessionAttributes == null) {
           sessionAttributes = new HashMap<>();
           headerAccessor.setSessionAttributes(sessionAttributes);
       }
       sessionAttributes.put("username", username);

       chatMessage.setType(MessageType.JOIN);

       // 업데이트된 사용자 목록 전송
       List<User> onlineUsers = userRepository.findByOnline(true);
       messagingTemplate.convertAndSend("/topic/users", onlineUsers);

       return chatMessage;
   }

   @MessageExceptionHandler
   @SendTo("/topic/errors")
   public String handleException(Throwable exception) {
       return exception.getMessage();
   }
}

