package com.example.web1.controller;

import com.example.web1.model.Answer;
import com.example.web1.model.ObjectiveSurvey;
import com.example.web1.model.SubjectiveSurvey;
import com.example.web1.service.SurveyService;
import com.example.web1.model.MemberEntity;
import com.example.web1.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/survey")
public class SurveyController {

  @Autowired
  private SurveyService surveyService;

  @Autowired
  private MemberRepository memberRepository;

  @Value("${app.server.address}")
  private String serverAddress;

  @GetMapping("/survey")
  public String getSurvey(Model model) {
    model.addAttribute("serverAddress", serverAddress);
    return "objectiveSurveyAnswer";
  }

  @GetMapping("/subjectiveSurvey")
  public String getSubjectiveSurveyForm(Model model) {
    model.addAttribute("serverAddress", serverAddress);
    return "subjectiveSurveyForm"; // 주관식 설문조사 등록 페이지로 이동
  }

  @GetMapping("/objectiveSurvey")
  public String getObjectiveSurveyForm(Model model) {
    model.addAttribute("serverAddress", serverAddress);
    return "objectiveSurveyForm"; // 객관식 설문조사 등록 페이지로 이동
  }

  @PostMapping("/submitObjective")
  public String submitObjectiveSurvey(
    @RequestParam String surveyTitle,
    @RequestParam String question,
    @RequestParam String option1,
    @RequestParam String option2,
    @RequestParam String option3,
    @RequestParam String option4,
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
    ObjectiveSurvey survey = new ObjectiveSurvey(surveyTitle, question, option1, option2, option3, option4, member);
    boolean isSaved = surveyService.saveObjectiveSurvey(survey, memberId);

    if (isSaved) {
      redirectAttributes.addFlashAttribute("message", "객관식 설문조사가 성공적으로 등록되었습니다!");
      return "redirect:/survey/objectiveSurveyAnswer/" + survey.getId();
    } else {
      redirectAttributes.addFlashAttribute("message", "객관식 설문조사 등록에 실패하였습니다.");
      return "redirect:/survey/failure";
    }
  }

  @GetMapping("/surveyChoice")
  public String getSurveyChoice(HttpSession session, Model model) {
    Long memberId = (Long) session.getAttribute("memberId");
    if (memberId == null) {
      model.addAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    List<SubjectiveSurvey> subjectiveSurveys = surveyService.getSubjectiveSurveysByMemberId(memberId);
    List<ObjectiveSurvey> objectiveSurveys = surveyService.getObjectiveSurveysByMemberId(memberId);

    model.addAttribute("subjectiveSurveys", subjectiveSurveys);
    model.addAttribute("objectiveSurveys", objectiveSurveys);
    return "surveychoice";
  }

  @PostMapping("/submitSubjective")
  public String submitSubjectiveSurvey(
    @RequestParam String surveyTitle,
    @RequestParam String question,
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
    SubjectiveSurvey survey = new SubjectiveSurvey(surveyTitle, question, member);
    boolean isSaved = surveyService.saveSubjectiveSurvey(survey, memberId);

    if (isSaved) {
      redirectAttributes.addFlashAttribute("message", "주관식 설문조사가 성공적으로 등록되었습니다!");
      return "redirect:/survey/subjectiveSurveyAnswer/" + survey.getId();
    } else {
      redirectAttributes.addFlashAttribute("message", "주관식 설문조사 등록에 실패하였습니다.");
      return "redirect:/survey/failure";
    }
  }

  @GetMapping("/subjectiveSurvey/{id}")
  public String getSubjectiveSurvey(@PathVariable Long id, @RequestParam(value = "tempSessionId", required = false) String tempSessionId, HttpSession session, Model model) {
    Long memberId = (Long) session.getAttribute("memberId");

    if (memberId == null && tempSessionId != null) {
      memberId = surveyService.validateTemporarySessionAndGetMemberId(tempSessionId);
      if (memberId != null) {
        session.setAttribute("memberId", memberId);
      }
    }

    if (memberId == null) {
      model.addAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    Optional<SubjectiveSurvey> survey = surveyService.getSubjectiveSurveyById(id);
    if (survey.isPresent()) {
      model.addAttribute("survey", survey.get());
      model.addAttribute("serverAddress", serverAddress);
      model.addAttribute("tempSessionId", tempSessionId); // tempSessionId를 모델에 추가
      return "subjectiveSurveyAnswer";
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/objectiveSurveyAnswer/{id}")
  public String getObjectiveSurveyAnswer(@PathVariable Long id, @RequestParam(value = "tempSessionId", required = false) String tempSessionId, HttpSession session, Model model) {
    Long memberId = (Long) session.getAttribute("memberId");

    if (memberId == null && tempSessionId != null) {
      memberId = surveyService.validateTemporarySessionAndGetMemberId(tempSessionId);
      if (memberId != null) {
        session.setAttribute("memberId", memberId);
      }
    }

    if (memberId == null) {
      model.addAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    Optional<ObjectiveSurvey> survey = surveyService.getObjectiveSurveyById(id);
    if (survey.isPresent()) {
      model.addAttribute("survey", survey.get());
      model.addAttribute("serverAddress", serverAddress);
      model.addAttribute("tempSessionId", tempSessionId); // tempSessionId를 모델에 추가

      // QR 코드 URL을 생성
      String qrCodeUrl = generateQRCodeUrl(memberId, "objectiveSurveyAnswer", id);
      model.addAttribute("qrCodeUrl", qrCodeUrl);

      return "objectiveSurveyAnswer";
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/subjectiveSurveyAnswer/{id}")
  public String getSubjectiveSurveyAnswer(@PathVariable Long id, @RequestParam(value = "tempSessionId", required = false) String tempSessionId, HttpSession session, Model model) {
    Long memberId = (Long) session.getAttribute("memberId");

    if (memberId == null && tempSessionId != null) {
      memberId = surveyService.validateTemporarySessionAndGetMemberId(tempSessionId);
      if (memberId != null) {
        session.setAttribute("memberId", memberId);
      }
    }

    if (memberId == null) {
      model.addAttribute("message", "로그인이 필요합니다.");
      return "redirect:/member/login";
    }

    Optional<SubjectiveSurvey> survey = surveyService.getSubjectiveSurveyById(id);
    if (survey.isPresent()) {
      model.addAttribute("survey", survey.get());
      model.addAttribute("serverAddress", serverAddress);
      model.addAttribute("tempSessionId", tempSessionId); // tempSessionId를 모델에 추가

      // QR 코드 URL을 생성
      String qrCodeUrl = generateQRCodeUrl(memberId, "subjectiveSurveyAnswer", id);
      model.addAttribute("qrCodeUrl", qrCodeUrl);

      return "subjectiveSurveyAnswer";
    } else {
      return "surveyNotFound";
    }
  }

  @PostMapping("/submitSubjectiveAnswer")
  public String submitSubjectiveAnswer(@RequestParam Long surveyId, @RequestParam String answer, RedirectAttributes redirectAttributes) {
    Optional<SubjectiveSurvey> survey = surveyService.getSubjectiveSurveyById(surveyId);
    if (survey.isPresent()) {
      Answer newAnswer = new Answer(survey.get(), answer);
      surveyService.saveAnswer(newAnswer);
      redirectAttributes.addFlashAttribute("result", "답변이 성공적으로 저장되었습니다!");
      return "redirect:/survey/subjectiveSurveyAnswer/" + surveyId;
    } else {
      return "surveyNotFound";
    }
  }

  @PostMapping("/submitObjectiveAnswer")
  public String submitObjectiveAnswer(@RequestParam Long surveyId, @RequestParam String answer, RedirectAttributes redirectAttributes) {
    Optional<ObjectiveSurvey> survey = surveyService.getObjectiveSurveyById(surveyId);
    if (survey.isPresent()) {
      Answer newAnswer = new Answer(survey.get(), answer);
      surveyService.saveAnswer(newAnswer);
      redirectAttributes.addFlashAttribute("result", "답변이 성공적으로 저장되었습니다!");
      return "redirect:/survey/objectiveSurveyAnswer/" + surveyId;
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/subjectiveSurveyResult/{id}")
  public String getSubjectiveSurveyResult(@PathVariable Long id, Model model) {
    Optional<SubjectiveSurvey> survey = surveyService.getSubjectiveSurveyById(id);
    if (survey.isPresent()) {
      List<Answer> answers = surveyService.getAnswersBySubjectiveSurveyId(id);
      model.addAttribute("survey", survey.get());
      model.addAttribute("answers", answers);
      return "subjectiveSurveyResult";
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/objectiveSurveyResult/{id}")
  public String getObjectiveSurveyResult(@PathVariable Long id, Model model) {
    Optional<ObjectiveSurvey> survey = surveyService.getObjectiveSurveyById(id);
    if (survey.isPresent()) {
      ObjectiveSurvey surveyData = survey.get();
      List<Answer> answers = surveyService.getAnswersByObjectiveSurveyId(id);
      Map<String, Long> answerCounts = answers.stream()
        .collect(Collectors.groupingBy(Answer::getAnswer, Collectors.counting()));

      // JSON 데이터를 문자열로 변환
      Map<String, Object> surveyDataJson = new HashMap<>();
      surveyDataJson.put("title", surveyData.getTitle());
      surveyDataJson.put("question", surveyData.getQuestion());
      surveyDataJson.put("options", List.of(surveyData.getOption1(), surveyData.getOption2(), surveyData.getOption3(), surveyData.getOption4()));
      surveyDataJson.put("answerCounts", answerCounts);

      try {
        model.addAttribute("surveyDataJson", new ObjectMapper().writeValueAsString(surveyDataJson));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to convert survey data to JSON", e);
      }

      model.addAttribute("survey", surveyData);
      model.addAttribute("answerCounts", answerCounts);
      return "objectiveSurveyResult";
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/result")
  public String showResultPage() {
    return "result"; // result.html로 매핑
  }

  @PostMapping("/deleteObjectiveSurvey/{id}")
  public String deleteObjectiveSurvey(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    Optional<ObjectiveSurvey> survey = surveyService.getObjectiveSurveyById(id);
    if (survey.isPresent()) {
      surveyService.deleteObjectiveSurveyById(id);
      redirectAttributes.addFlashAttribute("message", "객관식 설문조사가 성공적으로 삭제되었습니다.");
    } else {
      redirectAttributes.addFlashAttribute("message", "설문조사를 찾을 수 없습니다.");
    }
    return "redirect:/survey/surveyChoice";
  }

  @PostMapping("/deleteSubjectiveSurvey/{id}")
  public String deleteSubjectiveSurvey(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    Optional<SubjectiveSurvey> survey = surveyService.getSubjectiveSurveyById(id);
    if (survey.isPresent()) {
      surveyService.deleteSubjectiveSurveyById(id);
      redirectAttributes.addFlashAttribute("message", "주관식 설문조사가 성공적으로 삭제되었습니다.");
    } else {
      redirectAttributes.addFlashAttribute("message", "설문조사를 찾을 수 없습니다.");
    }
    return "redirect:/survey/surveyChoice";
  }

  // QR 코드 URL 생성 메서드
  private String generateQRCodeUrl(Long memberId, String purpose, Long id) {
    String tempSessionId = surveyService.createTemporarySession(memberId);
    switch (purpose) {
      case "fileDownload":
        return serverAddress + "/redirect-download?tempSessionId=" + tempSessionId;
      case "objectiveSurveyAnswer":
        return serverAddress + "/survey/objectiveSurveyAnswer/" + id + "?tempSessionId=" + tempSessionId;
      case "subjectiveSurveyAnswer":
        return serverAddress + "/survey/subjectiveSurveyAnswer/" + id + "?tempSessionId=" + tempSessionId;
      default:
        throw new IllegalArgumentException("Unknown purpose: " + purpose);
    }
  }
}
