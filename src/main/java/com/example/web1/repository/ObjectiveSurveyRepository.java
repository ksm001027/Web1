package com.example.web1.repository;

import com.example.web1.model.ObjectiveSurvey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveSurveyRepository extends CrudRepository<ObjectiveSurvey, Long> {
}
