package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

  @GetMapping("/")
  public String showIndex() {
    return "index";  // Thymeleaf가 'index.html'을 렌더링
  }

  // /uploadForm 매핑 제거
  // @GetMapping("/uploadForm")
  // public String showUploadForm() {
  //   return "uploadForm";  // 'uploadForm.html'을 렌더링
  // }

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
}
