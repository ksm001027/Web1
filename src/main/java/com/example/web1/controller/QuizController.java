package com.example.web1.controller;

import com.example.web1.model.ObjectiveQuiz;
import com.example.web1.model.SubjectiveQuiz;
import com.example.web1.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/quiz")
public class QuizController {

  @Autowired
  private QuizService quizService;

  @Value("${app.server.address}")
  private String serverAddress;

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
      return "redirect:/quiz/objectiveQuiz/" + quiz.getId(); // 퀴즈 ID로 이동
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
      return "redirect:/quiz/subjectiveQuiz/" + quiz.getId(); // 퀴즈 ID로 이동
    } else {
      redirectAttributes.addFlashAttribute("message", "주관식 퀴즈 등록에 실패하였습니다.");
      return "redirect:/quiz/failure";
    }
  }

  @GetMapping("/subjectiveQuiz/{id}")
  public String getSubjectiveQuiz(@PathVariable Long id, Model model) {
    Optional<SubjectiveQuiz> quiz = quizService.getSubjectiveQuizById(id);
    if (quiz.isPresent()) {
      model.addAttribute("quiz", quiz.get());
      model.addAttribute("serverAddress", serverAddress); // 서버 주소를 모델에 추가
      return "subjectiveQuiz"; // subjectiveQuiz.html로 매핑
    } else {
      return "quizNotFound";
    }
  }

  @PostMapping("/submitSubjectiveAnswer")
  public String submitSubjectiveAnswer(@RequestParam Long quizId, @RequestParam String answer, RedirectAttributes redirectAttributes) {
    Optional<SubjectiveQuiz> quiz = quizService.getSubjectiveQuizById(quizId);
    if (quiz.isPresent()) {
      if (quiz.get().getAnswer().equalsIgnoreCase(answer)) {
        redirectAttributes.addFlashAttribute("result", "정답입니다!");
      } else {
        redirectAttributes.addFlashAttribute("result", "오답입니다. 다시 시도하세요.");
      }
      return "redirect:/quiz/result";
    } else {
      return "quizNotFound";
    }
  }

  @GetMapping("/objectiveQuiz/{id}")
  public String getObjectiveQuiz(@PathVariable Long id, Model model) {
    Optional<ObjectiveQuiz> quiz = quizService.getObjectiveQuizById(id);
    if (quiz.isPresent()) {
      model.addAttribute("quiz", quiz.get());
      model.addAttribute("serverAddress", serverAddress); // 서버 주소를 모델에 추가
      return "objectiveQuiz"; // objectiveQuiz.html로 매핑
    } else {
      return "quizNotFound";
    }
  }

  @PostMapping("/submitObjectiveAnswer")
  public String submitObjectiveAnswer(@RequestParam Long quizId, @RequestParam int answer, RedirectAttributes redirectAttributes) {
    Optional<ObjectiveQuiz> quiz = quizService.getObjectiveQuizById(quizId);
    if (quiz.isPresent()) {
      if (quiz.get().getCorrectAnswerIndex() == answer) {
        redirectAttributes.addFlashAttribute("result", "정답입니다!");
      } else {
        redirectAttributes.addFlashAttribute("result", "오답입니다. 다시 시도하세요.");
      }
      return "redirect:/quiz/result";
    } else {
      return "quizNotFound";
    }
  }

  @GetMapping("/result")
  public String showResultPage() {
    return "result"; // result.html로 매핑
  }
}
