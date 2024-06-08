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

@Service
public class QuizService {

  @Autowired
  private SubjectiveQuizRepository subjectiveQuizRepository;

  @Autowired
  private ObjectiveQuizRepository objectiveQuizRepository;

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
    // Save the temp session to the database or a cache with the memberId
    return tempSessionId;
  }

  public Long validateTemporarySessionAndGetMemberId(String tempSessionId) {
    // Retrieve the memberId associated with the tempSessionId from the database or a cache
    return null;
  }

  // 삭제 메서드 추가
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
