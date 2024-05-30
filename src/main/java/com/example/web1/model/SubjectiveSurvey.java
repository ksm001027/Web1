package com.example.web1.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "subjective_survey")
public class SubjectiveSurvey {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String question;

  @OneToMany(mappedBy = "subjectiveSurvey", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Answer> answers;

  public SubjectiveSurvey() {
  }

  public SubjectiveSurvey(String title, String question) {
    this.title = title;
    this.question = question;
  }

  // Getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }
}
