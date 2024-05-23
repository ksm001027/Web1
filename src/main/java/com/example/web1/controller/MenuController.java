package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class MenuController {

  @GetMapping("/menu")
  public String showMenu(HttpSession session, Model model) {
    Long memberId = (Long) session.getAttribute("memberId");
    model.addAttribute("memberId", memberId);
    return "menu";
  }
}
