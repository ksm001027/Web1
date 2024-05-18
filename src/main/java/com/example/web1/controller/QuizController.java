package com.example.web1.controller;

import com.example.web1.model.ObjectiveQuiz;
import com.example.web1.model.SubjectiveQuiz;
import com.example.web1.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/quiz")
public class QuizController {

  @Autowired
  private QuizService quizService;

  @PostMapping("/submitObjective")
  public String submitObjectiveQuiz(
    @RequestParam String quizTitle,
    @RequestParam String question,
    @RequestParam String option1,
    @RequestParam String option2,
    @RequestParam String option3,
    @RequestParam String option4,
    @RequestParam int correctAnswer,
    RedirectAttributes redirectAttributes) {

    ObjectiveQuiz quiz = new ObjectiveQuiz(quizTitle, question, option1, option2, option3, option4, correctAnswer);
    boolean isSaved = quizService.saveObjectiveQuiz(quiz);

    if (isSaved) {
      redirectAttributes.addFlashAttribute("message", "객관식 퀴즈가 성공적으로 등록되었습니다!");
      return "redirect:/quiz/submitObjective";
    } else {
      redirectAttributes.addFlashAttribute("message", "객관식 퀴즈 등록에 실패하였습니다.");
      return "redirect:/quiz/failure";
    }
  }

  @PostMapping("/submitSubjective")
  public String submitSubjectiveQuiz(
    @RequestParam String quizTitle,
    @RequestParam String question,
    @RequestParam String answer,
    RedirectAttributes redirectAttributes) {

    SubjectiveQuiz quiz = new SubjectiveQuiz(quizTitle, question, answer);
    boolean isSaved = quizService.saveSubjectiveQuiz(quiz);

    if (isSaved) {
      redirectAttributes.addFlashAttribute("message", "주관식 퀴즈가 성공적으로 등록되었습니다!");
      return "redirect:/quiz1"; // 변경된 경로
    } else {
      redirectAttributes.addFlashAttribute("message", "주관식 퀴즈 등록에 실패하였습니다.");
      return "redirect:/quiz/failure";
    }
  }

}
