// src/main/java/com/example/chat/repository/ChatRepository.java

package com.example.cloneverytime.repository;

import com.example.cloneverytime.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<ChatMessage, String> {

}
