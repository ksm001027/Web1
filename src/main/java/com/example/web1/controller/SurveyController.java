package com.example.web1.controller;

import com.example.web1.model.Answer;
import com.example.web1.model.ObjectiveSurvey;
import com.example.web1.model.SubjectiveSurvey;
import com.example.web1.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/survey")
public class SurveyController {

  @Autowired
  private SurveyService surveyService;

  @PostMapping("/submitObjective")
  public String submitObjectiveSurvey(
    @RequestParam String surveyTitle,
    @RequestParam String question,
    @RequestParam String option1,
    @RequestParam String option2,
    @RequestParam String option3,
    @RequestParam String option4,
    RedirectAttributes redirectAttributes) {

    ObjectiveSurvey survey = new ObjectiveSurvey(surveyTitle, question, option1, option2, option3, option4);
    boolean isSaved = surveyService.saveObjectiveSurvey(survey);

    if (isSaved) {
      redirectAttributes.addFlashAttribute("message", "객관식 설문조사가 성공적으로 등록되었습니다!");
      return "redirect:/survey/objectiveSurveyAnswer/" + survey.getId(); // 설문조사 ID로 이동
    } else {
      redirectAttributes.addFlashAttribute("message", "객관식 설문조사 등록에 실패하였습니다.");
      return "redirect:/survey/failure";
    }
  }

  @PostMapping("/submitSubjective")
  public String submitSubjectiveSurvey(
    @RequestParam String surveyTitle,
    @RequestParam String question,
    RedirectAttributes redirectAttributes) {

    SubjectiveSurvey survey = new SubjectiveSurvey(surveyTitle, question);
    boolean isSaved = surveyService.saveSubjectiveSurvey(survey);

    if (isSaved) {
      redirectAttributes.addFlashAttribute("message", "주관식 설문조사가 성공적으로 등록되었습니다!");
      return "redirect:/survey/subjectiveSurveyAnswer/" + survey.getId(); // 설문조사 ID로 이동
    } else {
      redirectAttributes.addFlashAttribute("message", "주관식 설문조사 등록에 실패하였습니다.");
      return "redirect:/survey/failure";
    }
  }

  @GetMapping("/subjectiveSurvey/{id}")
  public String getSubjectiveSurvey(@PathVariable Long id, Model model) {
    Optional<SubjectiveSurvey> survey = surveyService.getSubjectiveSurveyById(id);
    if (survey.isPresent()) {
      model.addAttribute("survey", survey.get());
      return "subjectiveSurvey"; // subjectiveSurvey.html로 매핑
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/objectiveSurveyAnswer/{id}")
  public String getObjectiveSurveyAnswer(@PathVariable Long id, Model model) {
    Optional<ObjectiveSurvey> survey = surveyService.getObjectiveSurveyById(id);
    if (survey.isPresent()) {
      model.addAttribute("survey", survey.get());
      return "objectiveSurveyAnswer"; // objectiveSurveyAnswer.html로 매핑
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/subjectiveSurveyAnswer/{id}")
  public String getSubjectiveSurveyAnswer(@PathVariable Long id, Model model) {
    Optional<SubjectiveSurvey> survey = surveyService.getSubjectiveSurveyById(id);
    if (survey.isPresent()) {
      model.addAttribute("survey", survey.get());
      return "subjectiveSurveyAnswer"; // subjectiveSurveyAnswer.html로 매핑
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
      return "redirect:/survey/subjectiveSurveyAnswer/" + surveyId; // 다시 설문조사 답변 페이지로 리다이렉트
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
      return "redirect:/survey/objectiveSurveyAnswer/" + surveyId; // 결과 페이지로 리다이렉트
    } else {
      return "surveyNotFound";
    }
  }

<<<<<<< HEAD
=======
  @GetMapping("/subjectiveSurveyResult/{id}")
  public String getSubjectiveSurveyResult(@PathVariable Long id, Model model) {
    Optional<SubjectiveSurvey> survey = surveyService.getSubjectiveSurveyById(id);
    if (survey.isPresent()) {
      List<Answer> answers = surveyService.getAnswersBySubjectiveSurveyId(id);
      model.addAttribute("survey", survey.get());
      model.addAttribute("answers", answers);
      return "subjectiveSurveyResult"; // subjectiveSurveyResult.html로 매핑
    } else {
      return "surveyNotFound";
    }
  }

>>>>>>> 0d930cc1a657892d0c7aa70efda6b87451d2ec93
  @GetMapping("/objectiveSurveyResult/{id}")
  public String getObjectiveSurveyResult(@PathVariable Long id, Model model) {
    Optional<ObjectiveSurvey> survey = surveyService.getObjectiveSurveyById(id);
    if (survey.isPresent()) {
      List<Answer> answers = surveyService.getAnswersByObjectiveSurveyId(id);
      Map<String, Long> answerCounts = answers.stream()
        .collect(Collectors.groupingBy(Answer::getAnswer, Collectors.counting()));
      model.addAttribute("survey", survey.get());
      model.addAttribute("answerCounts", answerCounts);
      return "objectiveSurveyResult"; // objectiveSurveyResult.html로 매핑
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/result")
  public String showResultPage() {
    return "result"; // result.html로 매핑
  }
}
