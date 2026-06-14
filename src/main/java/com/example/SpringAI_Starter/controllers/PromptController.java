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

  @GetMapping("/few-shot")
  public String fewShot(@RequestParam String message){ // Accept a message as a query parameter
      String result= chatClient.prompt()
      .user(u -> u.text("""
      Tell me the time complexity of the algorithm based on the name given
      Only respond the time complexity or nothing else

        Examples:
        Algorithm: Bubble Sort
        Time Complexity: O(n^2)
        Algorithm: Merge Sort
        Time Complexity: O(n log n)

        Text: {algorithmName}

          """).param("algorithmName", message))
          .call()
          .content(); 

      return result;

  }
}