package com.example.cloneverytime.config;

import com.example.cloneverytime.model.User;
import com.example.cloneverytime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 변경된 부분: javax.annotation -> jakarta.annotation
import jakarta.annotation.PostConstruct;

@Component
public class UserStatusInitializer {

   @Autowired
   private UserRepository userRepository;

   @PostConstruct
   public void init() {
       // 모든 사용자의 online 상태를 false로 초기화
       for (User user : userRepository.findAll()) {
           user.setOnline(false);
           userRepository.save(user);
       }
       System.out.println("모든 사용자의 온라인 상태가 초기화되었습니다.");
   }
}
