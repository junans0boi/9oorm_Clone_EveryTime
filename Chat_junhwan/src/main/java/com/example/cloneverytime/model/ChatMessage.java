package com.example.cloneverytime.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chats")
public class ChatMessage {
   @Id
   private String id;

   private MessageType type;
   private String chat;

   private UserInfo user;

   private LocalDateTime createdAt = LocalDateTime.now();
   private LocalDateTime updatedAt = LocalDateTime.now();

   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public static class UserInfo {
       private String id;
       private String name;
   }
}
