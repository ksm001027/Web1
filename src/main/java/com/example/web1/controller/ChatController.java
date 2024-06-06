package com.example.web1.controller;

import com.example.web1.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class ChatController {

  @MessageMapping("/sendMessage/{roomId}")
  @SendTo("/topic/public/{roomId}")
  public ChatMessage sendMessage(ChatMessage chatMessage, @PathVariable String roomId) {
    return chatMessage;
  }

  @GetMapping("/{roomId}")
  public String chat(@PathVariable String roomId) {
    return "chat";
  }
}
