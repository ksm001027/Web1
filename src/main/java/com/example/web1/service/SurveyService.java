package com.example.web1.service;

import com.example.web1.model.Answer;
import com.example.web1.model.ObjectiveSurvey;
import com.example.web1.model.SubjectiveSurvey;
import com.example.web1.repository.AnswerRepository;
import com.example.web1.repository.ObjectiveSurveyRepository;
import com.example.web1.repository.SubjectiveSurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SurveyService {

  @Autowired
  private SubjectiveSurveyRepository subjectiveSurveyRepository;

  @Autowired
  private ObjectiveSurveyRepository objectiveSurveyRepository;

  @Autowired
  private AnswerRepository answerRepository;

  public boolean saveSubjectiveSurvey(SubjectiveSurvey survey) {
    try {
      subjectiveSurveyRepository.save(survey);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean saveObjectiveSurvey(ObjectiveSurvey survey) {
    try {
      objectiveSurveyRepository.save(survey);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Optional<SubjectiveSurvey> getSubjectiveSurveyById(Long id) {
    return subjectiveSurveyRepository.findById(id);
  }

  public Optional<ObjectiveSurvey> getObjectiveSurveyById(Long id) {
    return objectiveSurveyRepository.findById(id);
  }

  public void saveAnswer(Answer answer) {
    answerRepository.save(answer);
  }
}
