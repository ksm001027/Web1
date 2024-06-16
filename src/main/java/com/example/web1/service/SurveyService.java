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

  // 주관식 설문조사를 저장하는 메서드
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

  // 객관식 설문조사를 저장하는 메서드
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

  // 설문조사 ID로 주관식 설문조사를 가져오는 메서드
  public Optional<SubjectiveSurvey> getSubjectiveSurveyById(Long id) {
    return subjectiveSurveyRepository.findById(id);
  }

  // 설문조사 ID로 객관식 설문조사를 가져오는 메서드
  public Optional<ObjectiveSurvey> getObjectiveSurveyById(Long id) {
    return objectiveSurveyRepository.findById(id);
  }

  // 답변을 저장하는 메서드
  public void saveAnswer(Answer answer) {
    answerRepository.save(answer);
  }

  // 주관식 설문조사 ID로 답변을 가져오는 메서드
  public List<Answer> getAnswersBySubjectiveSurveyId(Long surveyId) {
    return answerRepository.findBySubjectiveSurveyId(surveyId);
  }

  // 객관식 설문조사 ID로 답변을 가져오는 메서드
  public List<Answer> getAnswersByObjectiveSurveyId(Long surveyId) {
    List<Answer> answers = answerRepository.findByObjectiveSurveyId(surveyId);
    System.out.println("Answers: " + answers);
    return answers;
  }

  // 객관식 설문조사의 답변을 집계하는 메서드
  public Map<String, Long> countAnswersForObjectiveSurvey(Long surveyId) {
    List<Answer> answers = answerRepository.findByObjectiveSurveyId(surveyId);
    return answers.stream().collect(Collectors.groupingBy(Answer::getAnswer, Collectors.counting()));
  }

  // 설문조사 ID로 모든 답변을 가져오는 메서드
  public List<Answer> getAnswersBySurveyId(Long surveyId) {
    List<Answer> subjectiveAnswers = answerRepository.findBySubjectiveSurveyId(surveyId);
    List<Answer> objectiveAnswers = answerRepository.findByObjectiveSurveyId(surveyId);
    subjectiveAnswers.addAll(objectiveAnswers);
    return subjectiveAnswers;
  }

  // 임시 세션을 생성하는 메서드
  public String createTemporarySession(Long memberId) {
    String tempSessionId = UUID.randomUUID().toString();
    tempSessionStore.put(tempSessionId, memberId);
    System.out.println("Created temp session: " + tempSessionId + " for memberId: " + memberId);
    return tempSessionId;
  }

  // 임시 세션 ID로 멤버 ID를 검증하는 메서드
  public Long validateTemporarySessionAndGetMemberId(String tempSessionId) {
    Long memberId = tempSessionStore.get(tempSessionId);
    System.out.println("Validated temp session: " + tempSessionId + ", memberId: " + memberId);
    return memberId;
  }

  // 멤버 ID로 주관식 설문조사를 가져오는 메서드
  public List<SubjectiveSurvey> getSubjectiveSurveysByMemberId(Long memberId) {
    return subjectiveSurveyRepository.findByMember_MemberId(memberId);
  }

  // 멤버 ID로 객관식 설문조사를 가져오는 메서드
  public List<ObjectiveSurvey> getObjectiveSurveysByMemberId(Long memberId) {
    return objectiveSurveyRepository.findByMember_MemberId(memberId);
  }

  // 주관식 설문조사를 삭제하는 메서드
  public void deleteSubjectiveSurveyById(Long id) {
    subjectiveSurveyRepository.deleteById(id);
  }

  // 객관식 설문조사를 삭제하는 메서드
  public void deleteObjectiveSurveyById(Long id) {
    objectiveSurveyRepository.deleteById(id);
  }
}
