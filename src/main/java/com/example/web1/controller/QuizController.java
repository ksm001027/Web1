package com.example.web1.controller;

import com.example.web1.model.ObjectiveQuiz;
import com.example.web1.model.SubjectiveQuiz;
import com.example.web1.model.MemberEntity;
import com.example.web1.repository.MemberRepository;
import com.example.web1.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quiz")
public class QuizController {

  @Autowired
  private QuizService quizService;

  @Autowired
  private MemberRepository memberRepository;

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
    HttpSession session,
    RedirectAttributes redirectAttributes) {

    Long memberId = (Long) session.getAttribute("memberId");
    if (memberId == null) {
      redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    Optional<MemberEntity> memberOpt = memberRepository.findById(memberId);
    if (!memberOpt.isPresent()) {
      redirectAttributes.addFlashAttribute("message", "유효하지 않은 사용자입니다.");
      return "redirect:/member/login";
    }

    MemberEntity member = memberOpt.get();
    ObjectiveQuiz quiz = new ObjectiveQuiz(quizTitle, question, option1, option2, option3, option4, correctAnswer, member);
    boolean isSaved = quizService.saveObjectiveQuiz(quiz);

    if (isSaved) {
      redirectAttributes.addFlashAttribute("message", "객관식 퀴즈가 성공적으로 등록되었습니다!");
      return "redirect:/quiz/objectiveQuiz/" + quiz.getId();
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
    HttpSession session,
    RedirectAttributes redirectAttributes) {

    Long memberId = (Long) session.getAttribute("memberId");
    if (memberId == null) {
      redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    Optional<MemberEntity> memberOpt = memberRepository.findById(memberId);
    if (!memberOpt.isPresent()) {
      redirectAttributes.addFlashAttribute("message", "유효하지 않은 사용자입니다.");
      return "redirect:/member/login";
    }

    MemberEntity member = memberOpt.get();
    SubjectiveQuiz quiz = new SubjectiveQuiz(quizTitle, question, answer, member);
    boolean isSaved = quizService.saveSubjectiveQuiz(quiz);

    if (isSaved) {
      redirectAttributes.addFlashAttribute("message", "주관식 퀴즈가 성공적으로 등록되었습니다!");
<<<<<<< HEAD
<<<<<<< HEAD
      return "redirect:/quiz1"; // 변경된 경로
=======
      return "redirect:/quiz/subjectiveQuiz/" + quiz.getId(); // 퀴즈 ID로 이동
>>>>>>> 14a438b65c4bd60893d34cea364262edd939db7d
=======
      return "redirect:/quiz/subjectiveQuiz/" + quiz.getId();
>>>>>>> bc916415288a5954341542681787419a7347a988
    } else {
      redirectAttributes.addFlashAttribute("message", "주관식 퀴즈 등록에 실패하였습니다.");
      return "redirect:/quiz/failure";
    }
  }

  @GetMapping("/quizChoice")
  public String getQuizChoice(HttpSession session, Model model) {
    Long memberId = (Long) session.getAttribute("memberId");
    if (memberId == null) {
      model.addAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    List<SubjectiveQuiz> subjectiveQuizzes = quizService.getSubjectiveQuizzesByMemberId(memberId);
    List<ObjectiveQuiz> objectiveQuizzes = quizService.getObjectiveQuizzesByMemberId(memberId);

    model.addAttribute("subjectiveQuizzes", subjectiveQuizzes);
    model.addAttribute("objectiveQuizzes", objectiveQuizzes);
    return "quizchoice";
  }

  @GetMapping("/subjectiveQuiz/{id}")
  public String getSubjectiveQuiz(@PathVariable Long id, @RequestParam(value = "tempSessionId", required = false) String tempSessionId, HttpSession session, Model model) {
    Long memberId = (Long) session.getAttribute("memberId");

    if (memberId == null && tempSessionId != null) {
      memberId = quizService.validateTemporarySessionAndGetMemberId(tempSessionId);
      if (memberId != null) {
        session.setAttribute("memberId", memberId);
      }
    }

    if (memberId == null) {
      model.addAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    Optional<SubjectiveQuiz> quiz = quizService.getSubjectiveQuizById(id);
    if (quiz.isPresent()) {
      model.addAttribute("quiz", quiz.get());
      model.addAttribute("serverAddress", serverAddress);
      model.addAttribute("tempSessionId", tempSessionId);

      // QR 코드 URL을 생성
      String qrCodeUrl = generateQRCodeUrl(memberId, "subjectiveQuiz", id);
      model.addAttribute("qrCodeUrl", qrCodeUrl);

      return "subjectiveQuiz";
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
  public String getObjectiveQuiz(@PathVariable Long id, @RequestParam(value = "tempSessionId", required = false) String tempSessionId, HttpSession session, Model model) {
    Long memberId = (Long) session.getAttribute("memberId");

    if (memberId == null && tempSessionId != null) {
      memberId = quizService.validateTemporarySessionAndGetMemberId(tempSessionId);
      if (memberId != null) {
        session.setAttribute("memberId", memberId);
      }
    }

    if (memberId == null) {
      model.addAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    Optional<ObjectiveQuiz> quiz = quizService.getObjectiveQuizById(id);
    if (quiz.isPresent()) {
      model.addAttribute("quiz", quiz.get());
      model.addAttribute("serverAddress", serverAddress);
      model.addAttribute("tempSessionId", tempSessionId);

      // QR 코드 URL을 생성
      String qrCodeUrl = generateQRCodeUrl(memberId, "objectiveQuiz", id);
      model.addAttribute("qrCodeUrl", qrCodeUrl);

      return "objectiveQuiz";
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
    return "result";
  }

  // 삭제 기능 추가
  @PostMapping("/deleteSubjectiveQuiz")
  public String deleteSubjectiveQuiz(@RequestParam Long quizId, HttpSession session, RedirectAttributes redirectAttributes) {
    Long memberId = (Long) session.getAttribute("memberId");
    if (memberId == null) {
      redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    boolean isDeleted = quizService.deleteSubjectiveQuiz(quizId, memberId);
    if (isDeleted) {
      redirectAttributes.addFlashAttribute("message", "주관식 퀴즈가 성공적으로 삭제되었습니다.");
    } else {
      redirectAttributes.addFlashAttribute("message", "주관식 퀴즈 삭제에 실패하였습니다.");
    }
    return "redirect:/quiz/quizChoice";
  }

  @PostMapping("/deleteObjectiveQuiz")
  public String deleteObjectiveQuiz(@RequestParam Long quizId, HttpSession session, RedirectAttributes redirectAttributes) {
    Long memberId = (Long) session.getAttribute("memberId");
    if (memberId == null) {
      redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    boolean isDeleted = quizService.deleteObjectiveQuiz(quizId, memberId);
    if (isDeleted) {
      redirectAttributes.addFlashAttribute("message", "객관식 퀴즈가 성공적으로 삭제되었습니다.");
    } else {
      redirectAttributes.addFlashAttribute("message", "객관식 퀴즈 삭제에 실패하였습니다.");
    }
    return "redirect:/quiz/quizChoice";
  }

  // QR 코드 URL 생성 메서드
  private String generateQRCodeUrl(Long memberId, String purpose, Long id) {
    String tempSessionId = quizService.createTemporarySession(memberId);
    switch (purpose) {
      case "fileDownload":
        return serverAddress + "/redirect-download?tempSessionId=" + tempSessionId;
      case "objectiveSurveyAnswer":
        return serverAddress + "/survey/objectiveSurveyAnswer/" + id + "?tempSessionId=" + tempSessionId;
      case "subjectiveSurveyAnswer":
        return serverAddress + "/survey/subjectiveSurveyAnswer/" + id + "?tempSessionId=" + tempSessionId;
      case "objectiveQuiz":
        return serverAddress + "/quiz/objectiveQuiz/" + id + "?tempSessionId=" + tempSessionId;
      case "subjectiveQuiz":
        return serverAddress + "/quiz/subjectiveQuiz/" + id + "?tempSessionId=" + tempSessionId;
      default:
        throw new IllegalArgumentException("Unknown purpose: " + purpose);
    }
  }
}
