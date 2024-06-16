package com.example.web1.service;

import com.example.web1.model.ObjectiveQuiz;
import com.example.web1.model.SubjectiveQuiz;
import com.example.web1.repository.ObjectiveQuizRepository;
import com.example.web1.repository.SubjectiveQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class QuizService {

  @Autowired
  private SubjectiveQuizRepository subjectiveQuizRepository;

  @Autowired
  private ObjectiveQuizRepository objectiveQuizRepository;

  // 임시 세션을 메모리에 저장하기 위한 간단한 맵
  private final Map<String, Long> temporarySessions = new ConcurrentHashMap<>();

  public boolean saveSubjectiveQuiz(SubjectiveQuiz quiz) {
    try {
      subjectiveQuizRepository.save(quiz);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean saveObjectiveQuiz(ObjectiveQuiz quiz) {
    try {
      objectiveQuizRepository.save(quiz);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Optional<SubjectiveQuiz> getSubjectiveQuizById(Long id) {
    return subjectiveQuizRepository.findById(id);
  }

  public Optional<ObjectiveQuiz> getObjectiveQuizById(Long id) {
    return objectiveQuizRepository.findById(id);
  }

  public List<SubjectiveQuiz> getSubjectiveQuizzesByMemberId(Long memberId) {
    return subjectiveQuizRepository.findByMember_MemberId(memberId);
  }

  public List<ObjectiveQuiz> getObjectiveQuizzesByMemberId(Long memberId) {
    return objectiveQuizRepository.findByMember_MemberId(memberId);
  }

  public String createTemporarySession(Long memberId) {
    String tempSessionId = UUID.randomUUID().toString();
    // 임시 세션을 맵에 저장 (일반적으로는 캐시나 데이터베이스에 저장)
    temporarySessions.put(tempSessionId, memberId);
    System.out.println("Created temp session: " + tempSessionId + " for memberId: " + memberId);
    return tempSessionId;
  }

  public Long validateTemporarySessionAndGetMemberId(String tempSessionId) {
    Long memberId = temporarySessions.get(tempSessionId);
    System.out.println("Validated temp session: " + tempSessionId + ", memberId: " + memberId);
    return memberId;
  }

  public boolean deleteSubjectiveQuiz(Long quizId, Long memberId) {
    Optional<SubjectiveQuiz> quizOpt = subjectiveQuizRepository.findById(quizId);
    if (quizOpt.isPresent() && quizOpt.get().getMember().getMemberId().equals(memberId)) {
      subjectiveQuizRepository.deleteById(quizId);
      return true;
    } else {
      return false;
    }
  }

  public boolean deleteObjectiveQuiz(Long quizId, Long memberId) {
    Optional<ObjectiveQuiz> quizOpt = objectiveQuizRepository.findById(quizId);
    if (quizOpt.isPresent() && quizOpt.get().getMember().getMemberId().equals(memberId)) {
      objectiveQuizRepository.deleteById(quizId);
      return true;
    } else {
      return false;
    }
  }
}
