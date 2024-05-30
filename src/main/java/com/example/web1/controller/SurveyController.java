package com.example.web1.controller;

import com.example.web1.model.ObjectiveSurvey;
import com.example.web1.model.SubjectiveSurvey;
import com.example.web1.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;
import org.springframework.ui.Model;

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
      return "redirect:/survey/objectiveSurvey/" + survey.getId(); // 설문조사 ID로 이동
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
      return "redirect:/survey/subjectiveSurvey/" + survey.getId(); // 설문조사 ID로 이동
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
      return "subjectiveSurveyAnswer"; // subjectiveSurveyAnswer.html로 매핑
    } else {
      return "surveyNotFound";
    }
  }

  @PostMapping("/submitSubjectiveAnswer")
  public String submitSubjectiveAnswer(@RequestParam Long surveyId, @RequestParam String answer, RedirectAttributes redirectAttributes) {
    Optional<SubjectiveSurvey> survey = surveyService.getSubjectiveSurveyById(surveyId);
    if (survey.isPresent()) {
      survey.get().setAnswer(answer);
      surveyService.saveSubjectiveSurvey(survey.get());
      redirectAttributes.addFlashAttribute("result", "주관식 설문조사의 답변이 저장되었습니다!");
      return "redirect:/survey/resultSubjective/" + surveyId;
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/resultSubjective/{id}")
  public String showSubjectiveResult(@PathVariable Long id, Model model) {
    Optional<SubjectiveSurvey> survey = surveyService.getSubjectiveSurveyById(id);
    if (survey.isPresent()) {
      model.addAttribute("survey", survey.get());
      return "resultSubjective"; // resultSubjective.html로 매핑
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/objectiveSurvey/{id}")
  public String getObjectiveSurvey(@PathVariable Long id, Model model) {
    Optional<ObjectiveSurvey> survey = surveyService.getObjectiveSurveyById(id);
    if (survey.isPresent()) {
      model.addAttribute("survey", survey.get());
      return "objectiveSurveyAnswer"; // objectiveSurveyAnswer.html로 매핑
    } else {
      return "surveyNotFound";
    }
  }

  @PostMapping("/submitObjectiveAnswer")
  public String submitObjectiveAnswer(@RequestParam Long surveyId, @RequestParam String selectedOption, RedirectAttributes redirectAttributes) {
    Optional<ObjectiveSurvey> survey = surveyService.getObjectiveSurveyById(surveyId);
    if (survey.isPresent()) {
      survey.get().setSelectedOption(selectedOption);
      surveyService.saveObjectiveSurvey(survey.get());
      redirectAttributes.addFlashAttribute("result", "객관식 설문조사의 답변이 저장되었습니다!");
      return "redirect:/survey/resultObjective/" + surveyId;
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/resultObjective/{id}")
  public String showObjectiveResult(@PathVariable Long id, Model model) {
    Optional<ObjectiveSurvey> survey = surveyService.getObjectiveSurveyById(id);
    if (survey.isPresent()) {
      model.addAttribute("survey", survey.get());
      return "resultObjective"; // resultObjective.html로 매핑
    } else {
      return "surveyNotFound";
    }
  }

  @GetMapping("/result")
  public String showResultPage() {
    return "result"; // result.html로 매핑
  }
}
