package com.example.web1.controller;

import com.example.web1.model.SubjectiveQuiz;
import com.example.web1.service.QuizTakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class QuizTakeController {

  @Autowired
  private QuizTakeService quizService;

  @GetMapping("/quiz/takeSubjective")
  public String takeSubjectiveQuiz(@RequestParam Long quizId, Model model) {
    SubjectiveQuiz quiz = quizService.findSubjectiveQuizById(quizId);
    if (quiz != null) {
      model.addAttribute("quiz", quiz);
      return "quiz1Take";
    } else {
      return "redirect:/quiz/notFound";
    }
  }

  @PostMapping("/quiz/submitSubjectiveAnswer")
  public String submitSubjectiveAnswer(
    @RequestParam Long quizId,
    @RequestParam String userAnswer,
    RedirectAttributes redirectAttributes) {

    boolean isCorrect = quizService.checkSubjectiveAnswer(quizId, userAnswer);
    if (isCorrect) {
      redirectAttributes.addFlashAttribute("message", "정답입니다!");
    } else {
      redirectAttributes.addFlashAttribute("message", "틀렸습니다. 다시 시도하세요.");
    }
    return "redirect:/quiz/takeSubjective?quizId=" + quizId;
  }
}
