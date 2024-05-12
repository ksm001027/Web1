package com.example.web1.service;

import com.example.web1.model.ObjectiveQuiz;
import com.example.web1.model.SubjectiveQuiz;
import com.example.web1.repository.ObjectiveQuizRepository;
import com.example.web1.repository.SubjectiveQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

  @Autowired
  private ObjectiveQuizRepository objectiveQuizRepository;

  @Autowired
  private SubjectiveQuizRepository subjectiveQuizRepository;

  public boolean saveObjectiveQuiz(ObjectiveQuiz quiz) {
    try {
      objectiveQuizRepository.save(quiz);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean saveSubjectiveQuiz(SubjectiveQuiz quiz) {
    try {
      subjectiveQuizRepository.save(quiz);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

}
