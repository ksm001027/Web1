package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
public class WebController {

<<<<<<< HEAD
=======
  @RequestMapping("/generate-random-chat")
  public String generateRandomChat() {
    String randomString = UUID.randomUUID().toString();
    return "redirect:/chat/" + randomString;
  }

  @GetMapping("/web-menu")
  public String webMenu() {
    return "menu"; // menu.html (기능 선택 페이지)
  }

<<<<<<< HEAD
>>>>>>> 14a438b65c4bd60893d34cea364262edd939db7d
  @GetMapping("/quizchoice")
  public String showQuizChoice() {
    return "quizchoice";  // 'quizchoice.html'을 렌더링
  }

  @GetMapping("/quiz1")
=======
  @GetMapping("/subjective")
>>>>>>> bc916415288a5954341542681787419a7347a988
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

<<<<<<< HEAD
  @GetMapping("/chat")
  public String showChatPage() {
    return "HTML/chat";  // 'chat.html'을 렌더링
  }

  //@GetMapping("/login")
  //public String login() { return "index"; // index.html (로그인 페이지)
  //}

=======
  @GetMapping("/login")
  public String login() {
    return "index"; // index.html (로그인 페이지)
  }
<<<<<<< HEAD
>>>>>>> 14a438b65c4bd60893d34cea364262edd939db7d
=======

  @GetMapping("/result")
  public String showResultPage() {
    return "result"; // result.html로 매핑
  }


  @GetMapping("/subjectiveSurvey")
  public String showSubjectiveSurvey() {
    return "subjectiveSurvey"; // subjectiveSurvey.html로 매핑
  }

  @GetMapping("/objectiveSurvey")
  public String showObjectiveSurvey() {
    return "objectiveSurvey"; // objectiveSurvey.html로 매핑
  }
>>>>>>> bc916415288a5954341542681787419a7347a988
}
