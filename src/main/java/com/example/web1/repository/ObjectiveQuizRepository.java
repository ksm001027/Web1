package com.example.web1.repository;

import com.example.web1.model.ObjectiveQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveQuizRepository extends JpaRepository<ObjectiveQuiz, Long> {
  List<ObjectiveQuiz> findByMember_MemberId(Long memberId);
}
