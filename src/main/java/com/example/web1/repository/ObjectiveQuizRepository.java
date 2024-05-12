// ObjectiveQuizRepository.java
package com.example.web1.repository;

import com.example.web1.model.ObjectiveQuiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveQuizRepository extends CrudRepository<ObjectiveQuiz, Long> {
  // Custom query methods can be defined here.
}
