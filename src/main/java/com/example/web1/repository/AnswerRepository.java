package com.example.web1.repository;

import com.example.web1.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
  List<Answer> findByObjectiveSurveyId(Long objectiveSurveyId);
  List<Answer> findBySubjectiveSurveyId(Long subjectiveSurveyId);
}
