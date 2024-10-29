// src/main/java/com/example/chat/controller/UserController.java

package com.example.cloneverytime.controller;

import com.example.cloneverytime.model.User;
import com.example.cloneverytime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

   @Autowired
   private UserRepository userRepository;

   @GetMapping("/online")
   public List<User> getOnlineUsers() {
       return userRepository.findByOnline(true);
   }
}
