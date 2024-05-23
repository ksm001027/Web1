package com.example.web1.repository;

import com.example.web1.model.SubjectiveQuiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectiveQuizRepository extends CrudRepository<SubjectiveQuiz, Long> {
}
