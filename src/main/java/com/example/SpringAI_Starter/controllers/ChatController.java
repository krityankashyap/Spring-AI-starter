package com.example.SpringAI_Starter.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SpringAI_Starter.dtos.MovieDto;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
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

  @GetMapping("/movies")
  public List<MovieDto> movieChat(@RequestParam String message, @RequestParam int count){
     return chatClient.prompt()
    .user(u->u.text("""
    You are a movie recommendation assistant. Based on the user's input, recommend {count} movies that match the description.
    User Input: {message}

        """).param("message", message).param("count", count))
        .call()
        .entity(new ParameterizedTypeReference<List<MovieDto>>() {});
  }

}



