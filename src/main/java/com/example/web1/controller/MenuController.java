package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Random;

@Controller
public class MenuController {

  @GetMapping("/menu")
  public String showMenu(Model model) {
    // 랜덤한 숫자를 생성하여 모델에 추가
    Random random = new Random();
    int roomId = random.nextInt(999999 - 100000) + 100000;
    model.addAttribute("roomId", roomId);
    return "menu";
  }
}
