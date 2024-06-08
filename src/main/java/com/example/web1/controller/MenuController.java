package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Random;

@Controller
public class MenuController {

  @GetMapping("/generate-chat-room")
  public String generateChatRoom(@RequestParam String title, Model model) {
    Random random = new Random();
    int roomId = random.nextInt(999999 - 100000) + 100000;
    String redirectUrl = UriComponentsBuilder.fromPath("/chat/{roomId}")
      .queryParam("title", title)
      .encode()  // URL 인코딩 추가
      .buildAndExpand(roomId)
      .toUriString();
    return "redirect:" + redirectUrl;
  }
}
