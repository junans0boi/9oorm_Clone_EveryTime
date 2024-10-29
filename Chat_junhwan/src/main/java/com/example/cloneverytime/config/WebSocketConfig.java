package com.example.cloneverytime.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

   @Override
   public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
       config.enableSimpleBroker("/topic", "/queue");
       config.setApplicationDestinationPrefixes("/app");
   }

   @Override
   public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
       registry.addEndpoint("/ws")
               .setAllowedOriginPatterns("*")
               .withSockJS()
                   .setSessionCookieNeeded(true);
   }
}
