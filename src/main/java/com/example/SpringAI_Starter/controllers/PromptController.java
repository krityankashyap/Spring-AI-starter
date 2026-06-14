package com.example.SpringAI_Starter.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class PromptController {
  
  private final ChatClient chatClient;

  public PromptController(ChatClient.Builder builder) {
    this.chatClient= builder.build();
  }

  @GetMapping("/zero-shot")
  public String zeroShot(@RequestParam String message){ // Accept a message as a query parameter
      String result= chatClient.prompt()
      .user(u -> u.text("""
      You are a helpful assistant that provides concise and accurate answers to user questions.

      Text: {message}
          """).param("message", message))
          .call()
          .content(); 

      return result;

  }