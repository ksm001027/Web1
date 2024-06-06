package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
public class WebController {

  @RequestMapping("/generate-random-chat")
  public String generateRandomChat() {
    String randomString = UUID.randomUUID().toString();
    return "redirect:/chat/" + randomString;
  }

  @GetMapping("/chat/{randomString}")
  public String showChatPage(@PathVariable String randomString) {
    return "chat"; // chat.html을 직접 반환 (templates 폴더에 있어야 함)
  }

  @GetMapping("/web-menu")
  public String webMenu() {
    return "menu"; // menu.html (기능 선택 페이지)
  }

  @GetMapping("/quizchoice")
  public String showQuizChoice() {
    return "quizchoice";  // 'quizchoice.html'을 렌더링
  }

  @GetMapping("/subjective")
  public String showQuiz1() {
    return "subjective";  //
  }

  @GetMapping("/quiz1Take")
  public String showquiz1Take() {
    return "quiz1Take";  // 'quiz1Take.html'을 렌더링
  }

  @GetMapping("/objective")
  public String showQuiz2() {
    return "objective";
  }

  @GetMapping("/quiz")
  public String showQuizPage() {
    return "quiz";  // 'quiz.html'을 렌더링
  }

  @GetMapping("/survey")
  public String showSurveyPage() {
    return "survey";  // 'survey.html'을 렌더링
  }

  @GetMapping("/login")
  public String login() {
    return "index"; // index.html (로그인 페이지)
  }

  @GetMapping("/result")
  public String showResultPage() {
    return "result"; // result.html로 매핑
  }

  @GetMapping("/surveychoice")
  public String showSurveyChoice() {
    return "surveychoice";  // 'surveychoice.html'을 렌더링
  }

  @GetMapping("/subjectiveSurvey")
  public String showSubjectiveSurvey() {
    return "subjectiveSurvey"; // subjectiveSurvey.html로 매핑
  }

  @GetMapping("/objectiveSurvey")
  public String showObjectiveSurvey() {
    return "objectiveSurvey"; // objectiveSurvey.html로 매핑
  }
}
