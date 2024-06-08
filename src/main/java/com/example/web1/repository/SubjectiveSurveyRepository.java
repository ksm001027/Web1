package com.example.web1.repository;

import com.example.web1.model.SubjectiveSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectiveSurveyRepository extends JpaRepository<SubjectiveSurvey, Long> {
  List<SubjectiveSurvey> findByMember_MemberId(Long memberId);
}
