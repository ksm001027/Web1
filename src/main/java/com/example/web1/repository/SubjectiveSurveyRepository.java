package com.example.web1.repository;

import com.example.web1.model.SubjectiveSurvey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectiveSurveyRepository extends CrudRepository<SubjectiveSurvey, Long> {
}
