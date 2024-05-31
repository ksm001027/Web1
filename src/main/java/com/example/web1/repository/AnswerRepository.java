package com.example.web1.repository;

import com.example.web1.model.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {
  List<Answer> findByObjectiveSurveyId(Long objectiveSurveyId);
  List<Answer> findBySubjectiveSurveyId(Long subjectiveSurveyId);
}
