package com.example.web1.repository;

import com.example.web1.model.SubjectiveQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectiveQuizRepository extends JpaRepository<SubjectiveQuiz, Long> {
  List<SubjectiveQuiz> findByMember_MemberId(Long memberId);
}
