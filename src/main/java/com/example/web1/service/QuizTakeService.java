package com.example.web1.service;

import com.example.web1.model.SubjectiveQuiz;
import com.example.web1.repository.SubjectiveQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizTakeService {

  @Autowired
  private SubjectiveQuizRepository subjectiveQuizRepository;

  public SubjectiveQuiz findSubjectiveQuizById(Long quizId) {
    return subjectiveQuizRepository.findById(quizId).orElse(null);
  }

  public boolean checkSubjectiveAnswer(Long quizId, String userAnswer) {
    SubjectiveQuiz quiz = subjectiveQuizRepository.findById(quizId).orElse(null);
    if (quiz != null && quiz.getAnswer().equalsIgnoreCase(userAnswer.trim())) {
      return true;
    }
    return false;
  }
}
