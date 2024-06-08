package com.example.web1.repository;

import com.example.web1.model.ObjectiveSurvey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveSurveyRepository extends JpaRepository<ObjectiveSurvey, Long> {
  List<ObjectiveSurvey> findByMember_MemberId(Long memberId);
}
