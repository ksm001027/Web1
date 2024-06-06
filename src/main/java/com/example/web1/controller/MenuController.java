package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Random;

@Controller
public class MenuController {

  @GetMapping("/generate-random-chat")
  public String generateRandomChat(Model model) {
    Random random = new Random();
    int roomId = random.nextInt(999999 - 100000) + 100000;
    return "redirect:/chat/" + roomId;
  }
}
