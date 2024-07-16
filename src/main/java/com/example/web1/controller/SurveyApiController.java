package com.example.web1.controller;

import com.example.web1.model.Answer;
import com.example.web1.model.ObjectiveSurvey;
import com.example.web1.model.SubjectiveSurvey;
import com.example.web1.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/survey")
public class SurveyApiController {

  @Autowired
  private SurveyService surveyService;

  // 주관식 설문조사 데이터를 JSON으로 반환하는 API
  @GetMapping("/subjective/{id}")
  public Map<String, Object> getSubjectiveSurveyData(@PathVariable Long id) {
    Optional<SubjectiveSurvey> survey = surveyService.getSubjectiveSurveyById(id);
    Map<String, Object> response = new HashMap<>();
    if (survey.isPresent()) {
      response.put("title", survey.get().getTitle());
      response.put("question", survey.get().getQuestion());
      List<Answer> answers = surveyService.getAnswersBySubjectiveSurveyId(id);
      response.put("answers", answers.stream().map(Answer::getAnswer).collect(Collectors.toList()));
    } else {
      response.put("error", "Survey not found");
    }
    return response;
  }

  // 객관식 설문조사 데이터를 JSON으로 반환하는 API
  @GetMapping("/objective/{id}")
  public Map<String, Object> getObjectiveSurveyData(@PathVariable Long id) {
    Optional<ObjectiveSurvey> survey = surveyService.getObjectiveSurveyById(id);
    Map<String, Object> response = new HashMap<>();
    if (survey.isPresent()) {
      ObjectiveSurvey objectiveSurvey = survey.get();

      response.put("title", objectiveSurvey.getTitle());
      response.put("question", objectiveSurvey.getQuestion());

      // 개별 옵션들을 배열로 변환하여 추가
      List<String> options = List.of(
        objectiveSurvey.getOption1(),
        objectiveSurvey.getOption2(),
        objectiveSurvey.getOption3(),
        objectiveSurvey.getOption4()
      );
      response.put("options", options);

      // 각 답변에 대한 응답 수를 계산하여 추가
      List<Answer> answers = surveyService.getAnswersByObjectiveSurveyId(id);
      Map<String, Long> answerCounts = answers.stream()
        .collect(Collectors.groupingBy(Answer::getAnswer, Collectors.counting()));
      response.put("answerCounts", answerCounts);
    } else {
      response.put("error", "Survey not found");
    }
    return response;
  }
}
