package com.example.SpringAI_Starter.controllers;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/guardrail")
public class GuardRailController {
  
  private final ChatClient chatClient;

  public GuardRailController(ChatClient.Builder builder){
    this.chatClient= builder
    .defaultSystem("You are a helpful assistant that always responds in a positive and encouraging manner.")
    .defaultAdvisors(SafeGuardAdvisor.builder()
    .sensitiveWords(List.of("hate", "evil", "rape", "exploit", "kill")).build())
    .build();
  }

  @GetMapping("/chat")
  public String chatWithGuardian(@RequestParam String message){
      String response= chatClient.prompt()
      .user(message)
      .call()
      .content();

      return response;
  }
  

}
