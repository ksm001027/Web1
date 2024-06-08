package com.example.web1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "objective_survey_id", insertable = false, updatable = false)
  private ObjectiveSurvey objectiveSurvey;

  @ManyToOne
  @JoinColumn(name = "subjective_survey_id", insertable = false, updatable = false)
  private SubjectiveSurvey subjectiveSurvey;

  @Column(name = "objective_survey_id")
  private Long objectiveSurveyId;

  @Column(name = "subjective_survey_id")
  private Long subjectiveSurveyId;

  @Column(nullable = false)
  private String answer;

  public Answer() {
  }

  public Answer(ObjectiveSurvey objectiveSurvey, String answer) {
    this.objectiveSurvey = objectiveSurvey;
    this.objectiveSurveyId = objectiveSurvey.getId();
    this.answer = answer;
  }

  public Answer(SubjectiveSurvey subjectiveSurvey, String answer) {
    this.subjectiveSurvey = subjectiveSurvey;
    this.subjectiveSurveyId = subjectiveSurvey.getId();
    this.answer = answer;
  }

  // Getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ObjectiveSurvey getObjectiveSurvey() {
    return objectiveSurvey;
  }

  public void setObjectiveSurvey(ObjectiveSurvey objectiveSurvey) {
    this.objectiveSurvey = objectiveSurvey;
  }

  public Long getObjectiveSurveyId() {
    return objectiveSurveyId;
  }

  public void setObjectiveSurveyId(Long objectiveSurveyId) {
    this.objectiveSurveyId = objectiveSurveyId;
  }

  public SubjectiveSurvey getSubjectiveSurvey() {
    return subjectiveSurvey;
  }

  public void setSubjectiveSurvey(SubjectiveSurvey subjectiveSurvey) {
    this.subjectiveSurvey = subjectiveSurvey;
  }

  public Long getSubjectiveSurveyId() {
    return subjectiveSurveyId;
  }

  public void setSubjectiveSurveyId(Long subjectiveSurveyId) {
    this.subjectiveSurveyId = subjectiveSurveyId;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }
}
