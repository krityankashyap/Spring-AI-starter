package com.example.SpringAI_Starter.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController

@RequestMapping("/api/chat")
public class ChatController {

  private final ChatClient chatClient;

  
  public ChatController(ChatClient.Builder builder){
    this.chatClient= builder.build();
  }

  @GetMapping("/message")
  public String simpleChat(@RequestParam String message){
      String response = chatClient.prompt()
      .user(message)
      .call()
      .content(); // Retrieve the response body

      return response;
  }
}

