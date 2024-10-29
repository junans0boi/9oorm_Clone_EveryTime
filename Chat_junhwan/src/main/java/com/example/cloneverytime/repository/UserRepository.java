// src/main/java/com/example/chat/repository/UserRepository.java

package com.example.cloneverytime.repository;

import com.example.cloneverytime.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
   User findByName(String name);
   List<User> findByOnline(Boolean online);
}
