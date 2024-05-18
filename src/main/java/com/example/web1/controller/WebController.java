package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

  @GetMapping("/")
  public String showIndex() {
    return "index";  // Thymeleaf가 'index.html'을 렌더링
  }

  @GetMapping("/quizchoice")
  public String showQuizChoice() {
    return "quizchoice";  // 'quizchoice.html'을 렌더링
  }

  @GetMapping("/quiz1")
  public String showQuiz1() {
    return "quiz1";  // 'quizchoice.html'을 렌더링
  }

  @GetMapping("/quiz1Take")
  public String showquiz1Take() {
    return "quiz1Take";  // 'quizchoice.html'을 렌더링
  }

  @GetMapping("/quiz2")
  public String showQuiz2() {
    return "quiz2";  // 'quizchoice.html'을 렌더링
  }

  @GetMapping("/quiz")
  public String showQuizPage() {
    return "HTML/quiz";  // 'quiz.html'을 렌더링
  }

  @GetMapping("/survey")
  public String showSurveyPage() {
    return "HTML/survey";  // 'survey.html'을 렌더링
  }

  @GetMapping("/chat")
  public String showChatPage() {
    return "HTML/chat";  // 'chat.html'을 렌더링
  }

  @GetMapping("/login")
  public String login() { return "index"; // index.html (로그인 페이지)
  }

}
