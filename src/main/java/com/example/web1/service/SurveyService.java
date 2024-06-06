package com.example.web1.service;

import com.example.web1.model.Answer;
import com.example.web1.model.ObjectiveSurvey;
import com.example.web1.model.SubjectiveSurvey;
import com.example.web1.model.MemberEntity;
import com.example.web1.repository.AnswerRepository;
import com.example.web1.repository.ObjectiveSurveyRepository;
import com.example.web1.repository.SubjectiveSurveyRepository;
import com.example.web1.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SurveyService {

  @Autowired
  private SubjectiveSurveyRepository subjectiveSurveyRepository;

  @Autowired
  private ObjectiveSurveyRepository objectiveSurveyRepository;

  @Autowired
  private AnswerRepository answerRepository;

  @Autowired
  private MemberRepository memberRepository;

  private Map<String, Long> tempSessionStore = new ConcurrentHashMap<>();

  public boolean saveSubjectiveSurvey(SubjectiveSurvey survey, Long memberId) {
    try {
      Optional<MemberEntity> memberOpt = memberRepository.findById(memberId);
      if (memberOpt.isPresent()) {
        survey.setMember(memberOpt.get());
        subjectiveSurveyRepository.save(survey);
        return true;
      } else {
        return false; // Member not found
      }
    } catch (Exception e) {
      return false;
    }
  }

  public boolean saveObjectiveSurvey(ObjectiveSurvey survey, Long memberId) {
    try {
      Optional<MemberEntity> memberOpt = memberRepository.findById(memberId);
      if (memberOpt.isPresent()) {
        survey.setMember(memberOpt.get());
        objectiveSurveyRepository.save(survey);
        return true;
      } else {
        return false; // Member not found
      }
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

  public List<Answer> getAnswersBySubjectiveSurveyId(Long surveyId) {
    return answerRepository.findBySubjectiveSurveyId(surveyId);
  }

  public List<Answer> getAnswersByObjectiveSurveyId(Long surveyId) {
    List<Answer> answers = answerRepository.findByObjectiveSurveyId(surveyId);
    System.out.println("Answers: " + answers);
    return answers;
  }

  public Map<String, Long> countAnswersForObjectiveSurvey(Long surveyId) {
    List<Answer> answers = answerRepository.findByObjectiveSurveyId(surveyId);
    return answers.stream().collect(Collectors.groupingBy(Answer::getAnswer, Collectors.counting()));
  }

  public List<Answer> getAnswersBySurveyId(Long surveyId) {
    List<Answer> subjectiveAnswers = answerRepository.findBySubjectiveSurveyId(surveyId);
    List<Answer> objectiveAnswers = answerRepository.findByObjectiveSurveyId(surveyId);
    subjectiveAnswers.addAll(objectiveAnswers);
    return subjectiveAnswers;
  }

  public String createTemporarySession(Long memberId) {
    String tempSessionId = UUID.randomUUID().toString();
    tempSessionStore.put(tempSessionId, memberId);
    return tempSessionId;
  }
  public List<SubjectiveSurvey> getSubjectiveSurveysByMemberId(Long memberId) {
    return subjectiveSurveyRepository.findByMember_MemberId(memberId);
  }

  public List<ObjectiveSurvey> getObjectiveSurveysByMemberId(Long memberId) {
    return objectiveSurveyRepository.findByMember_MemberId(memberId);
  }


  public Long validateTemporarySessionAndGetMemberId(String tempSessionId) {
    return tempSessionStore.get(tempSessionId);
  }
}
