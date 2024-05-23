package com.example.web1.service;

import com.example.web1.model.ObjectiveQuiz;
import com.example.web1.model.SubjectiveQuiz;
import com.example.web1.repository.ObjectiveQuizRepository;
import com.example.web1.repository.SubjectiveQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
