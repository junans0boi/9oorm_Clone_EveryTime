// src/main/java/com/example/chat/model/User.java

package com.example.cloneverytime.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
   @Id
   private String id;

   private String name;
   private String token;
   private Boolean online = false;
}
