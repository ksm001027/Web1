package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import com.example.web1.service.UserService;

@Controller
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/login")
  public String login() {
    return "index"; // index.html 파일 반환
  }

  @PostMapping("/loginAction")
  public String loginAction(@RequestParam("userID") String userID,
                            @RequestParam("userPassword") String userPassword,
                            HttpSession session, Model model) {
    int result = userService.login(userID, userPassword);
    if (result == 1) {
      session.setAttribute("userID", userID);
      return "redirect:/menu";
    } else {
      String message = "";
      switch (result) {
        case 0:
          message = "비밀번호가 틀립니다.";
          break;
        case -1:
          message = "존재하지 않는 아이디입니다.";
          break;
      }
      model.addAttribute("message", message);
      return "index";
    }
  }

  @GetMapping("/menu")
  public String menu() {
    return "menu"; // main.html 파일 반환
  }
}
